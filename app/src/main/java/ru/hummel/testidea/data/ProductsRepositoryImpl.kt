package ru.hummel.testidea.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.hummel.testidea.data.model.ProductDto
import ru.hummel.testidea.data.source.local.ProductDao
import ru.hummel.testidea.data.source.local.entities.ProductEntity

class ProductsRepositoryImpl(
  private val productDao: ProductDao,
) : ProductsRepository {

  override val data: Flow<PagingData<ProductDto>> = Pager(
    config = PagingConfig(pageSize = 25),
    pagingSourceFactory = productDao::pagingSource,
  ).flow.map { pagingData ->
    pagingData.map(ProductEntity::toDto)
  }


  override suspend fun add(productDto: ProductDto) {
    productDao.insert(product = ProductEntity.fromDto(productDto))
  }

  override suspend fun delete(productDto: ProductDto) {
    productDao.removeById(productDto.id)
  }

  override suspend fun edit(productDto: ProductDto) {
    productDao.insert(product = ProductEntity.fromDto(productDto))
  }


}