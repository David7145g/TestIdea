package ru.hummel.testidea.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.hummel.testidea.data.model.ProductDto

interface ProductsRepository {

  val data: Flow<PagingData<ProductDto>>

  suspend fun add(productDto: ProductDto)

  suspend fun delete(productDto: ProductDto)

  suspend fun edit(productDto: ProductDto)
}