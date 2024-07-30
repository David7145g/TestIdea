package ru.hummel.testidea.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.hummel.testidea.data.source.local.AppDb
import ru.hummel.testidea.data.source.local.ProductDao
import ru.hummel.testidea.data.source.local.ProductRemoteKeyDao
import ru.hummel.testidea.data.source.local.entities.ProductEntity
import ru.hummel.testidea.data.source.local.entities.ProductRemoteKeyEntity
import ru.hummel.testidea.data.source.local.entities.toEntity
import ru.hummel.testidea.data.source.remote.ApiService
import ru.hummel.testidea.data.source.remote.Response


@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
  private val service: ApiService,
  private val db: AppDb,
  private val productDao: ProductDao,
  private val productRemoteKeyDao: ProductRemoteKeyDao,
) : RemoteMediator<Int, ProductEntity>() {
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, ProductEntity>
  ): MediatorResult {
    try {
      val response = when (loadType) {
        LoadType.REFRESH -> service.getLatest(state.config.initialLoadSize)
        LoadType.PREPEND -> {
          val id = productRemoteKeyDao.max() ?: return MediatorResult.Success(
            endOfPaginationReached = false
          )
          service.getAfter(id, state.config.pageSize)
        }

        LoadType.APPEND -> {
          val id = productRemoteKeyDao.min() ?: return MediatorResult.Success(
            endOfPaginationReached = false
          )
          service.getBefore(id, state.config.pageSize)
        }
      }

      if (response is Response.Error) {
        throw ApiService.ApiError(response.errorCode, response.errorMessage)
      }
      val body = (response as Response.Success).data

      db.withTransaction {
        when (loadType) {
          LoadType.REFRESH -> {
            productRemoteKeyDao.removeAll()
            productRemoteKeyDao.insert(
              listOf(
                ProductRemoteKeyEntity(
                  type = ProductRemoteKeyEntity.KeyType.AFTER,
                  id = body.first().id,
                ),
                ProductRemoteKeyEntity(
                  type = ProductRemoteKeyEntity.KeyType.BEFORE,
                  id = body.last().id,
                ),
              )
            )
            productDao.removeAll()
          }

          LoadType.PREPEND -> {
            productRemoteKeyDao.insert(
              ProductRemoteKeyEntity(
                type = ProductRemoteKeyEntity.KeyType.AFTER,
                id = body.first().id,
              )
            )
          }

          LoadType.APPEND -> {
            productRemoteKeyDao.insert(
              ProductRemoteKeyEntity(
                type = ProductRemoteKeyEntity.KeyType.BEFORE,
                id = body.last().id,
              )
            )
          }
        }
        productDao.insert(body.toEntity())
      }
      return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
    } catch (e: Exception) {
      return MediatorResult.Error(e)
    }
  }
}