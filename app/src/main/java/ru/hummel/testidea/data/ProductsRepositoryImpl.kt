package ru.hummel.testidea.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hummel.testidea.data.model.Product
import ru.hummel.testidea.data.source.local.AppDb
import ru.hummel.testidea.data.source.local.ProductRemoteKeyDao
import ru.hummel.testidea.data.source.local.ProductDao
import ru.hummel.testidea.data.source.local.entities.ProductEntity
import ru.hummel.testidea.data.source.remote.ApiService

class ProductsRepositoryImpl(
  apiService: ApiService,
  appDb: AppDb,
  productDao: ProductDao,
  productRemoteKeyDao: ProductRemoteKeyDao
) : ProductsRepository {

  @OptIn(ExperimentalPagingApi::class)
  override val data: Flow<PagingData<Product>> = Pager(
    config = PagingConfig(pageSize = 25),
    remoteMediator = PostRemoteMediator(apiService, appDb, productDao, productRemoteKeyDao),
    pagingSourceFactory = productDao::pagingSource,
    ).flow.map { pagingData ->
    pagingData.map(ProductEntity::toDto)
  }


  override suspend fun getAll() {
    TODO("Not yet implemented")
  }

}