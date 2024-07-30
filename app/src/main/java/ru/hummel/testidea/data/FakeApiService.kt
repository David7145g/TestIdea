package ru.hummel.testidea.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import ru.hummel.testidea.data.model.Product
import ru.hummel.testidea.data.source.local.AppDb
import ru.hummel.testidea.data.source.local.entities.ProductEntity
import ru.hummel.testidea.data.source.local.entities.toDto
import ru.hummel.testidea.data.source.remote.ApiService
import ru.hummel.testidea.data.source.remote.Response

/**
 * Ложная реализация remote Api, которая на самом деле берет данные из файла data.db
 */
class FakeApiService(
  private val context: Context
) : ApiService {

  private val database: AppDb by lazy {
    Room.databaseBuilder(
      context.applicationContext,
      AppDb::class.java,
      "data.db"
    ).build()
  }

  override suspend fun getBefore(id: Long, count: Int): Response<List<Product>> {

    return Response.Success(
      database.fakeProductDao().getProductsBefore(id, count).toDto()
    )
  }

  override suspend fun getAfter(id: Long, count: Int): Response<List<Product>> {
    return Response.Success(
      database.fakeProductDao().getProductsAfter(id, count).toDto()
    )
  }

  override suspend fun getLatest(count: Int): Response<List<Product>> {
    return Response.Success(
      database.fakeProductDao().getProducts(count).toDto()
    )
  }

}