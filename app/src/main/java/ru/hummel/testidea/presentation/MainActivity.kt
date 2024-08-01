package ru.hummel.testidea.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import ru.hummel.pethealth.presentation.navigation.AppNavigation
import ru.hummel.testidea.presentation.ui.theme.Shapes
import ru.hummel.testidea.presentation.ui.theme.TestIdeaTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {

      val appState = rememberAppState()

      TestIdeaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {

          Scaffold(
            snackbarHost = {
              SnackbarHost(
                modifier = Modifier,
                hostState = appState.snackbarHostState,
                snackbar = { snackbarData ->
                  Snackbar(
                    modifier = Modifier,
                    snackbarData = snackbarData,
                    shape = Shapes.small
                  )
                }

              )
            },
            bottomBar = { Box {}}
          ) { innerPaddingModifier ->

            AppNavigation(
              modifier = Modifier.padding(innerPaddingModifier),
              appState = appState,
            )
          }
        }
      }
    }
  }
}
