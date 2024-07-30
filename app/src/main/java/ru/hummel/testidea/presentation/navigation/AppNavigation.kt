package ru.hummel.pethealth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.hummel.testidea.presentation.AppState
import ru.hummel.testidea.presentation.features.home.HomeRoute
import ru.hummel.testidea.presentation.features.home.homeScreen

@Composable
fun AppNavigation(
  modifier: Modifier = Modifier,
  appState: AppState,
) {
  NavHost(
    modifier = modifier,
    navController = appState.navController,
    startDestination = HomeRoute,
  ) {
    homeScreen()
  }
}
