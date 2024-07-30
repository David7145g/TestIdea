package ru.hummel.testidea.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.hummel.testidea.data.model.Product

@Entity
data class ProductEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int,
  val name: String,
  val time: Int,
  val tags: List<String>,
  val amount: Int,
){

  fun toDto() = Product(id, name, time, tags, amount)

  companion object {
    fun fromDto(dto: Product) =
      ProductEntity(dto.id, dto.name, dto.time, dto.tags, dto.amount,)
  }
}

fun List<ProductEntity>.toDto(): List<Product> = map(ProductEntity::toDto)
fun List<Product>.toEntity(): List<ProductEntity> = map(ProductEntity::fromDto)