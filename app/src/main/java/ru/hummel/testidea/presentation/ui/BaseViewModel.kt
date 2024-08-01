package ru.hummel.testidea.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.hummel.testidea.presentation.ui.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.hummel.testidea.presentation.ui.snackbar.SnackbarMessage.Companion.toSnackbarMessage

open class BaseViewModel : ViewModel() {

  protected fun launchCatchingViewModelScope(
    snackbar: Boolean = true,
    block: suspend CoroutineScope.() -> Unit
  ) = viewModelScope.launch(
    CoroutineExceptionHandler { _, throwable ->
      if (snackbar) {
        Log.e("David", "CoroutineExceptionHandler|Base VM")
        SnackbarManager.showMessage(throwable.toSnackbarMessage())
      }
    },
    block = block
  )

}