package ru.hummel.testidea.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.hummel.testidea.data.source.local.entities.ProductRemoteKeyEntity

@Dao
interface ProductRemoteKeyDao {
  @Query("SELECT COUNT(*) == 0 FROM ProductRemoteKeyEntity")
  suspend fun isEmpty(): Boolean

  @Query("SELECT MAX(id) FROM ProductRemoteKeyEntity")
  suspend fun max(): Long?

  @Query("SELECT MIN(id) FROM ProductRemoteKeyEntity")
  suspend fun min(): Long?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(key: ProductRemoteKeyEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(keys: List<ProductRemoteKeyEntity>)

  @Query("DELETE FROM ProductRemoteKeyEntity")
  suspend fun removeAll()
}