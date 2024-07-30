package ru.hummel.testidea.data.source.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductRemoteKeyEntity(
  @PrimaryKey
  val type: KeyType,
  val id: Int,
) {
  enum class KeyType {
    AFTER, BEFORE
  }
}