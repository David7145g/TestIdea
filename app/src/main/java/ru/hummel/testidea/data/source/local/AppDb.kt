package ru.hummel.testidea.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.hummel.testidea.data.source.local.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
@TypeConverters(AppDb.Converters::class)
abstract class AppDb : RoomDatabase() {

  abstract fun productDao(): ProductDao


  class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
      return value.split(",")
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
      return list.joinToString(",")
    }
  }
}