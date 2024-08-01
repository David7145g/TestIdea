package ru.hummel.testidea.di

import android.content.Context
import androidx.room.Room
import ru.hummel.testidea.data.ProductsRepository
import ru.hummel.testidea.data.ProductsRepositoryImpl
import ru.hummel.testidea.data.source.local.AppDb

interface AppModule {
  val productsRepository: ProductsRepository
  val appDb: AppDb
}


fun AppModule(appContext: Context): AppModule {
  return object : AppModule {
    override val productsRepository: ProductsRepository by lazy {
      ProductsRepositoryImpl(
        productDao = appDb.productDao(),
      )
    }
    override val appDb: AppDb by lazy {
      provideDb(appContext)
    }
  }
}


private fun provideDb(context: Context): AppDb {
  return Room.databaseBuilder(context, AppDb::class.java, "app.db")
    .fallbackToDestructiveMigration()
    .build()
}