package ru.hummel.testidea.data.model

data class Product(
  val id: Int,
  val name: String,
  val time: Int,
  val tags: List<String>,
  val amount: Int,
)