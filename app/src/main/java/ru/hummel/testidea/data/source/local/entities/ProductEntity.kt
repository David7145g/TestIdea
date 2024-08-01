package ru.hummel.testidea.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.hummel.testidea.data.model.ProductDto
import ru.hummel.testidea.data.source.local.AppDb
import ru.hummel.testidea.common.date.epochMillisToLocalDateTime
import ru.hummel.testidea.common.date.localDateTimeToEpochMillis

@Entity
data class ProductEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val name: String,
  val time: Long,
  @TypeConverters(AppDb.Converters::class)
  val tags: List<String> = listOf(),
  val amount: Int,
) {

  fun toDto(): ProductDto {
    val localDateTime = epochMillisToLocalDateTime(time)
    return ProductDto(id, name, localDateTime, tags, amount)
  }

  companion object {
    fun fromDto(dto: ProductDto): ProductEntity {
      val epochMillis = localDateTimeToEpochMillis(dto.time)
      return ProductEntity(id = dto.id, name = dto.name, time = epochMillis, tags = dto.tags, amount = dto.amount)
    }

  }
}

fun List<ProductEntity>.toDto(): List<ProductDto> = map(ProductEntity::toDto)
fun List<ProductDto>.toEntity(): List<ProductEntity> = map(ProductEntity::fromDto)