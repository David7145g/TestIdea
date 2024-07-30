package ru.hummel.testidea.presentation.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.hummel.testidea.data.ProductsRepository
import ru.hummel.testidea.data.model.Product
import ru.hummel.testidea.presentation.ui.BaseViewModel

class HomeViewModel(
  private val repository: ProductsRepository,
) : BaseViewModel() {

  val data: Flow<PagingData<Product>> = repository.data.cachedIn(viewModelScope)

  init {
//    loadProducts()
  }

//  fun loadProducts() = launchCatchingViewModelScope {
//    try {
//      _dataState.value = FeedModelState(loading = true)
//      // repository.stream.cachedIn(viewModelScope).
//      _dataState.value = FeedModelState()
//    } catch (e: Exception) {
//      _dataState.value = FeedModelState(error = true)
//    }
//  }

}