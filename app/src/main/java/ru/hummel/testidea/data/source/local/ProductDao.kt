package ru.hummel.testidea.data.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.hummel.testidea.data.source.local.entities.ProductEntity

@Dao
interface ProductDao {

  @Query("SELECT * FROM ProductEntity ORDER BY id DESC")
  fun pagingSource(): PagingSource<Int, ProductEntity>

  @Query("SELECT * FROM ProductEntity ORDER BY id DESC")
  fun getAll(): Flow<List<ProductEntity>>

  @Query("SELECT COUNT(*) == 0 FROM ProductEntity")
  suspend fun isEmpty(): Boolean

  @Query("SELECT COUNT(*) FROM ProductEntity")
  suspend fun count(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(post: ProductEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(posts: List<ProductEntity>)

  @Query("DELETE FROM ProductEntity WHERE id = :id")
  suspend fun removeById(id: Long)

  @Query("DELETE FROM ProductEntity")
  suspend fun removeAll()
}