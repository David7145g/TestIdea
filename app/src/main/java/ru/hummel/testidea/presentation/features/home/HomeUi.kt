package ru.hummel.testidea.presentation.features.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flow
import ru.hummel.testidea.TestIdeaApplication
import ru.hummel.testidea.data.model.Product

@Composable
fun HomeScreen() {
  val viewModel = viewModel<HomeViewModel>(factory = viewModelFactory {
    HomeViewModel(
      repository = TestIdeaApplication.appModule.productsRepository
    )
  })

  val lazyPagingItems = viewModel.data.collectAsLazyPagingItems()

  HomeUi(
    lazyPagingItems = lazyPagingItems
  )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeUi(
  lazyPagingItems: LazyPagingItems<Product>
) {

  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  Scaffold(
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      MediumTopAppBar(
        title = { Text(text = "Список Товаров") },
        scrollBehavior = scrollBehavior
      )
    }
  ) { innerPadding ->
    LazyColumn(modifier = Modifier.padding(innerPadding)) {

      items(lazyPagingItems.itemSnapshotList) { product ->
        if (product != null) {
          ProductItem(product)
        }
      }
    }
  }
}

@Composable
fun ProductItem(product: Product) {
  // Ваш Composable для отображения элемента
  Text(text = product.name)
}

@Preview
@Composable
private fun HomePreview() {
  HomeUi(
    lazyPagingItems = mockPagingItems()
  )
}

@Composable
fun mockPagingItems(): LazyPagingItems<Product> {
  val mockData = listOf(
    Product(id = 1, name = "Sheila Middleton", time = 1222, tags = listOf(), amount = 8698),
    Product(id = 2, name = "Owen Hester", time = 1476, tags = listOf(), amount = 9918),
    Product(id = 3, name = "Bridgett Hurley", time = 2323, tags = listOf(), amount = 6338)
  )
  val pagingData = flow<PagingData<Product>> { PagingData.from(mockData) }
  return pagingData.collectAsLazyPagingItems()
}