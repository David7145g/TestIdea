package ru.hummel.testidea.data.source.remote

import ru.hummel.testidea.data.model.Product

interface ApiService {

  class ApiError(code: Int, message: String) : Exception(message)

  suspend fun getBefore(
    id: Long,
    count: Int
  ): Response<List<Product>>

  suspend fun getAfter(
    id: Long,
    count: Int
  ): Response<List<Product>>

  suspend fun getLatest(
    count: Int
  ): Response<List<Product>>
}

sealed interface Response<T> {
  data class Success<T>(val data: T) : Response<T>
  data class Error<T>(val errorCode: Int, val errorMessage: String) : Response<T>
}