package ru.hummel.testidea.presentation.features.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import ru.hummel.testidea.R
import ru.hummel.testidea.TestIdeaApplication
import ru.hummel.testidea.common.date.toMyFormat
import ru.hummel.testidea.data.model.ProductDto
import ru.hummel.testidea.di.viewModelHelper
import ru.hummel.testidea.presentation.features.home.HomeViewModel.HomeAction
import ru.hummel.testidea.presentation.features.home.HomeViewModel.HomeAction.AddProduct
import ru.hummel.testidea.presentation.ui.theme.EDGE_BY_COLUMN_TOKEN
import ru.hummel.testidea.presentation.ui.theme.PRODUCT_ITEM_MARGIN_TOKEN
import ru.hummel.testidea.presentation.ui.theme.PRODUCT_ITEM_SPACED_BY_COLUMN_TOKEN
import ru.hummel.testidea.presentation.ui.theme.SPACED_BY_COLUMN_TOKEN
import ru.hummel.testidea.R.string as AppText


@Composable
fun HomeScreen() {
  val viewModel = viewModel<HomeViewModel>(factory = viewModelHelper {
    HomeViewModel(
      repository = TestIdeaApplication.appModule.productsRepository
    )
  })

  val lazyPagingItems = viewModel.products
  val isSearching by viewModel.isSearching.collectAsState()
  val searchText by viewModel.searchText.collectAsState()

  HomeUi(
    flow = lazyPagingItems,
    onAction = viewModel::handleAction,
    isSearching = isSearching,
    searchText = searchText,
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeUi(
  flow: Flow<PagingData<ProductDto>>,
  onAction: (HomeAction) -> Unit,
  isSearching: Boolean,
  searchText: String,
) {
  val lazyPagingItems = flow.collectAsLazyPagingItems()
  val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

  var showAddDialog by remember { mutableStateOf(false) }
  var showEditDialog by remember { mutableStateOf(false) }
  var showDeleteDialog by remember { mutableStateOf(false) }

  var selectedProduct by rememberSaveable { mutableStateOf<ProductDto?>(null) }

  val editProduct: (ProductDto) -> Unit = { product ->
    selectedProduct = product
    showEditDialog = true
  }

  val deleteProduct: (ProductDto) -> Unit = { product ->
    selectedProduct = product
    showDeleteDialog = true
  }

  if (showAddDialog) {
    AddProductDialog(
      onAdd = { product ->
        onAction(product)
        showAddDialog = false
      },
      onCancel = {
        showAddDialog = false
      }
    )
  }

  if (showEditDialog) {
    selectedProduct?.let {

      EditProductAmountDialog(
        productDto = it,
        onEdit = { action ->
          onAction(action)
          showEditDialog = false
        },
        onCancel = {
          showEditDialog = false
        }
      )
    }
  }

  if (showDeleteDialog) {
    selectedProduct?.let {
      DeleteProductDialog(
        productDto = it,
        onDelete = { action ->
          onAction(action)
          showDeleteDialog = false
        },
        onCancel = {
          showDeleteDialog = false
        }
      )
    }
  }

  Scaffold(
    modifier = Modifier
      .nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      MediumTopAppBar(
        title = { Text(text = "Список Товаров") },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors()
      )
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { showAddDialog = true },
      ) {
        Icon(Icons.Filled.Add, Icons.Filled.Add.name)
      }
    },
    bottomBar = { Box {} }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(
        SPACED_BY_COLUMN_TOKEN, Alignment.CenterVertically
      ),
      contentPadding = PaddingValues(
        start = EDGE_BY_COLUMN_TOKEN,
        end = EDGE_BY_COLUMN_TOKEN,
        bottom = 50.dp
      )
    ) {
      item {
        var isSearchActive by rememberSaveable { mutableStateOf(false) }

        EmbeddedSearchBar(
          modifier = Modifier,
          searchQuery = searchText,
          onQueryChange = { query ->
            onAction(HomeAction.ChangeSearchQuery(query))
          },
          isSearchActive = isSearchActive,
          onActiveChanged = { isSearchActive = it },
          isLoading = isSearching,
        )
      }
      items(lazyPagingItems.itemSnapshotList) { item ->
        if (item != null) {
          ProductItem(
            productDto = item,
            onEditClick = { product ->
              editProduct(product)
            },
            onDeleteClick = { product ->
              deleteProduct(product)
            }
          )
        }
      }
    }
  }
}

@Composable
fun AddProductDialog(
  onAdd: (HomeAction) -> Unit,
  onCancel: () -> Unit
) {
  var productName by remember { mutableStateOf("") }
  var productTags by remember { mutableStateOf("") }
  var productAmount by remember { mutableStateOf(0) }

  AlertDialog(
    onDismissRequest = onCancel,
    title = { Text(stringResource(R.string.add_product)) },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedTextField(
          value = productName,
          onValueChange = { value ->
            productName = value
          },
          label = { Text(stringResource(R.string.product_name)) },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = productTags,
          onValueChange = { value ->
            productTags = value
          },
          label = { Text(stringResource(R.string.tags_comma_separated)) },
          modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
          value = productAmount.toString(),
          onValueChange = { value ->
            productAmount = ((value.toIntOrNull() ?: 0))
          },
          label = { Text(stringResource(R.string.amount)) },
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onAdd(
            AddProduct(
              name = productName,
              tags = productTags,
              amount = productAmount
            )
          )
        }
      ) {
        Text(stringResource(R.string.add))
      }
    },
    dismissButton = {
      TextButton(onClick = onCancel) {
        Text(stringResource(R.string.cancel))
      }
    }
  )
}

@Composable
fun EditProductAmountDialog(
  productDto: ProductDto,
  onEdit: (HomeAction) -> Unit,
  onCancel: () -> Unit
) {

  var amount by remember { mutableStateOf(productDto.amount) }

  AlertDialog(
    onDismissRequest = onCancel,
    title = { Text(stringResource(R.string.count_product)) },
    text = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = {
          if (amount > 1) {
            amount--
          }
        }) {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = null
          )
        }
        Text(text = amount.toString())
        IconButton(onClick = { amount++ }) {
          Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null
          )
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onEdit(HomeAction.EditAmount(productDto, amount))
        }
      ) {
        Text(stringResource(R.string.confirm))
      }
    },
    dismissButton = {
      TextButton(onClick = onCancel) {
        Text(stringResource(R.string.cancel))
      }
    }
  )
}

@Composable
fun DeleteProductDialog(
  productDto: ProductDto,
  onDelete: (HomeAction) -> Unit,
  onCancel: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onCancel,
    title = { Text(stringResource(R.string.delete_this_item)) },
    text = {
      Text(stringResource(R.string.delete_this_item))
    },
    confirmButton = {
      TextButton(
        onClick = {
          onDelete(
            HomeAction.Delete(productDto)
          )
        }
      ) {
        Text(stringResource(R.string.delete))
      }
    },
    dismissButton = {
      TextButton(onClick = onCancel) {
        Text(stringResource(R.string.cancel))
      }
    }
  )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductItem(
  productDto: ProductDto,
  onEditClick: (ProductDto) -> Unit,
  onDeleteClick: (ProductDto) -> Unit,
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(
      Modifier.padding(PRODUCT_ITEM_MARGIN_TOKEN),
      verticalArrangement = Arrangement.spacedBy(
        PRODUCT_ITEM_SPACED_BY_COLUMN_TOKEN,
        Alignment.CenterVertically
      )
    ) {

      Row(
        modifier = Modifier
          .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Text(
          modifier = Modifier.weight(1f),
          text = productDto.name,
          style = MaterialTheme.typography.titleMedium
        )
        IconButton(onClick = { onEditClick(productDto) }) {
          Icon(imageVector = Icons.Default.Edit, contentDescription = Icons.Default.Edit.name)
        }
        IconButton(onClick = { onDeleteClick(productDto) }) {
          Icon(imageVector = Icons.Default.Delete, contentDescription = Icons.Default.Delete.name)
        }
      }
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
        verticalArrangement = Arrangement.Top,
      ) {
        productDto.tags.forEach { tag ->
          SuggestionChip(
            onClick = {  },
            label = {
              Text(text = tag)
            }
          )
        }
      }
      Row(
        modifier = Modifier.fillMaxWidth(),
      ) {
        ProductInfoText(
          modifier = Modifier.weight(1f),
          title = stringResource(AppText.in_stock),
          info = productDto.amount.toString()
        )
        ProductInfoText(
          modifier = Modifier.weight(1f),
          title = stringResource(R.string.date_added),
          info = productDto.time.toMyFormat()
        )
      }
    }
  }
}

@Composable
fun ProductInfoText(
  modifier: Modifier,
  title: String,
  info: String
) {
  Text(
    modifier = modifier,
    text = buildAnnotatedString {
      withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
        append(title)
      }
      append("\n")
      append(info)
    },
    style = MaterialTheme.typography.bodySmall,
    color = Color.Black,
    textAlign = TextAlign.Start
  )
}

@Composable
fun EmbeddedSearchBar(
  modifier: Modifier = Modifier,
  searchQuery: String,
  onQueryChange: (String) -> Unit,
  isSearchActive: Boolean,
  onActiveChanged: (Boolean) -> Unit,
  isLoading: Boolean,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {

  val focusManager = LocalFocusManager.current
  val isFocused = interactionSource.collectIsFocusedAsState().value
  val shouldClearFocus = !isSearchActive && isFocused
  val focusRequester = remember { FocusRequester() }

  LaunchedEffect(isSearchActive) {
    if (shouldClearFocus) {
      delay(100L)
      focusManager.clearFocus()
    }
  }

  OutlinedTextField(
    modifier = modifier
      .fillMaxWidth()
      .focusRequester(focusRequester)
      .onFocusChanged { if (it.isFocused) onActiveChanged(true) },
    value = searchQuery,
    onValueChange = { query -> onQueryChange(query) },
    leadingIcon = {
      LeadingIconForSearch(isSearchActive = isSearchActive, activeChanged = onActiveChanged)
    },
    trailingIcon = {
      if (isSearchActive && searchQuery.isNotEmpty()) {
        TrailingIconForSearch(isLoading = isLoading, onQueryChange = onQueryChange)
      }
    },
    maxLines = 1,
    singleLine = true,
    placeholder = { Text("Поиск") },
    interactionSource = interactionSource
  )

  BackHandler(enabled = isSearchActive) {
    onActiveChanged(false)
  }
}

@Composable
fun TrailingIconForSearch(isLoading: Boolean, onQueryChange: (String) -> Unit) {
  if (isLoading) {
    CircularProgressIndicator()
  } else {
    IconButton(
      onClick = {
        onQueryChange("")
      },
    ) {
      Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  }
}

@Composable
fun LeadingIconForSearch(isSearchActive: Boolean, activeChanged: (Boolean) -> Unit) {
  if (isSearchActive) {
    IconButton(
      onClick = { activeChanged(false) },
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
    }
  } else {
    Icon(
      imageVector = Icons.Rounded.Search,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}

@Preview(
  showBackground = true,
  showSystemUi = true,
  locale = "RU"
)
@Composable
private fun HomePreview() {
  HomeUi(
    flow = mockPagingItems(),
    onAction = {},
    isSearching = false,
    searchText = "",
  )
}

fun mockPagingItems(): Flow<PagingData<ProductDto>> {
  val time = LocalDateTime.parse("01.01.21 1:00", DateTimeFormatter.ofPattern("dd.MM.yy H:mm"))
  val mockData = listOf(
    ProductDto(
      id = 1,
      name = "Sheila Middleton",
      time = time,
      tags = listOf("Телефон", "Новый", "Распродажа"),
      amount = 8698
    ),
    ProductDto(
      id = 2,
      name = "Owen Hester",
      time = time,
      tags = listOf("Телефон", "Хит"),
      amount = 9918
    ),
    ProductDto(
      id = 3,
      name = "Bridgett Hurley",
      time = time,
      tags = listOf("Игровая приставка", "Акция", "Распродажа"),
      amount = 6338
    )
  )
  val pagingData = MutableStateFlow(PagingData.from(mockData))
  return pagingData
}
