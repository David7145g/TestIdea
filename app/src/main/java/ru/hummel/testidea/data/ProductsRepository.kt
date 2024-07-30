package ru.hummel.testidea.data

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.hummel.testidea.data.model.Product

interface ProductsRepository {

  val data: Flow<PagingData<Product>>
  suspend fun getAll()

}