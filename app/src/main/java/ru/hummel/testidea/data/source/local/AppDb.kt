package ru.hummel.testidea.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.hummel.testidea.data.FakeProductDao
import ru.hummel.testidea.data.source.local.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {

  abstract fun productDao(): ProductDao
  abstract fun productRemoteKeyDao(): ProductRemoteKeyDao

  abstract fun fakeProductDao(): FakeProductDao

}