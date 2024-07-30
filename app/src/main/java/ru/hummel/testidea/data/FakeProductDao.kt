package ru.hummel.testidea.data

import androidx.room.Dao
import androidx.room.Query
import ru.hummel.testidea.data.source.local.entities.ProductEntity
import ru.hummel.testidea.data.source.remote.ApiService

@Dao
interface FakeProductDao : ApiService {

  @Query("SELECT * FROM ProductEntity ORDER BY id DESC LIMIT :pageSize")
  suspend fun getProducts(pageSize: Int): List<ProductEntity>

  @Query("SELECT * FROM ProductEntity WHERE id > :id ORDER BY id DESC LIMIT :pageSize")
  suspend fun getProductsAfter(id: Long, pageSize: Int): List<ProductEntity>

  @Query("SELECT * FROM ProductEntity WHERE id < :id ORDER BY id DESC LIMIT :pageSize")
  suspend fun getProductsBefore(id: Long, pageSize: Int): List<ProductEntity>

}