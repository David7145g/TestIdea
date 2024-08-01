package ru.hummel.testidea.data.model

import org.threeten.bp.LocalDateTime


data class ProductDto(
  val id: Int = 0,
  val name: String,
  val time: LocalDateTime,
  val tags: List<String>,
  val amount: Int,
)