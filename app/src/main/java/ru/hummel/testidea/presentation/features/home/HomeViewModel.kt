package ru.hummel.testidea.presentation.features.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import org.threeten.bp.LocalDateTime
import ru.hummel.testidea.data.ProductsRepository
import ru.hummel.testidea.data.model.ProductDto
import ru.hummel.testidea.presentation.ui.BaseViewModel
import ru.hummel.testidea.presentation.ui.snackbar.SnackbarManager
import  ru.hummel.testidea.R.string as AppText


class HomeViewModel(
  private val repository: ProductsRepository,
) : BaseViewModel() {

  private val data: Flow<PagingData<ProductDto>> = repository.data
    .cachedIn(viewModelScope)

  private val _searchText = MutableStateFlow("")
  val searchText = _searchText.asStateFlow()

  private val _isSearching = MutableStateFlow(false)
  val isSearching = _isSearching.asStateFlow()

  @OptIn(FlowPreview::class)
  val products : Flow<PagingData<ProductDto>> = searchText
    .debounce(500L)
    .onEach { _isSearching.emit(true) }
    .combine(data) { text, data ->
      if (text.isBlank()) {
        data
      } else {
        data.filter { it.name.contains(text) }
      }
    }
    .onEach { _isSearching.emit(false) }


  /**
   * В данном случае делаю костыли, потому что по нормальному нужно создавать адаптеры для фичей
   * и маппить данные, чтобы не было логики в data class'ах и при этом не работать с моделями из Ui
   * на данном этапе это слишком разрастит код, поэтому делаю так
   */

  fun handleAction(action: HomeAction) {
    when (action) {

      is HomeAction.AddProduct -> add(action)
      is HomeAction.Delete -> delete(action)
      is HomeAction.EditAmount -> edit(action)
      is HomeAction.ChangeSearchQuery -> search(action)
    }
  }

  private fun search(action: HomeAction.ChangeSearchQuery){
    _searchText.value = action.query
  }

  // Здесь нужно писать большой юзкейс с user friendly AppException и проверкой на валидность модельки
  private fun add(action: HomeAction.AddProduct) = launchCatchingViewModelScope {
    // тут мне уже было лень выносить строки в ресурсы
    if (action.name.isBlank()) throw Exception("Имя товара не должно быть пустым")
    if (action.tags.split(',').isEmpty()) throw Exception("Добавьте тег")
    if (action.amount < 1) throw Exception("Кол-во должно быть положительным")
    val productDto = ProductDto(
      name = action.name,
      time = LocalDateTime.now(),
      tags = action.tags.split(','),
      amount = action.amount
    )
    repository.add(productDto)
    SnackbarManager.showMessage(AppText.success)
  }

  private fun edit(action: HomeAction.EditAmount) = launchCatchingViewModelScope {
    // поддержать консистентность данных через бд, тут хардкожу, ибо начну уже скоро такими темпами
    // писать продукт, а не демку
    val product = action.product.copy(amount = action.newAmount)
    repository.edit(product)
    SnackbarManager.showMessage(AppText.success)
  }

  private fun delete(action: HomeAction.Delete) = launchCatchingViewModelScope {
    repository.delete(action.product)
    SnackbarManager.showMessage(AppText.success)
  }

  sealed interface HomeAction {

    data class AddProduct(val name: String, val tags: String, val amount: Int) : HomeAction

    data class ChangeSearchQuery(val query: String) : HomeAction

    data class EditAmount(val product: ProductDto, val newAmount: Int) : HomeAction

    data class Delete(val product: ProductDto) : HomeAction
  }
}