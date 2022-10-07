package com.ramcosta.destinations.sample

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.boodschappenlijst.toast
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.destinations.sample.core.viewmodel.activityViewModel
import com.ramcosta.destinations.sample.destinations.Destination
import com.ramcosta.destinations.sample.destinations.LoginScreenDestination
import com.ramcosta.destinations.sample.ui.composables.BottomBar
import com.ramcosta.destinations.sample.ui.composables.SampleScaffold
import com.ramcosta.destinations.sample.ui.composables.TopBar

@OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
@Composable
fun SampleApp() {
    val engine = rememberAnimatedNavHostEngine()
    val navController = engine.rememberNavController()

    val vm = activityViewModel<MainViewModel>()
    // 👇 this avoids a jump in the UI that would happen if we relied only on ShowLoginWhenLoggedOut
    val startRoute = if (!vm.isLoggedIn) LoginScreenDestination else NavGraphs.root.startRoute
    val ctx = LocalContext.current

    SampleScaffold(
        navController = navController,
        startRoute = startRoute,
        topBar = { dest, backStackEntry ->
            //toast(ctx, "topBar creeren ???")
            if (dest.shouldShowScaffoldElements) {
                TopBar(dest, backStackEntry)
            }
        },
        bottomBar = {
            if (it.shouldShowScaffoldElements) {
                BottomBar(navController)
            }
        }
    ) {
        toast(ctx, "SampleScaffold")
        DestinationsNavHost(
            engine = engine,
            navController = navController,
            navGraph = NavGraphs.root,
            modifier = Modifier.padding(it),
            startRoute = startRoute
        )
        toast(ctx, "go_to_ShowLoginWhenLoggedOut")
        // Has to be called after calling DestinationsNavHost because only
        // then does NavController have a graph associated that we need for
        // `appCurrentDestinationAsState` method
        ShowLoginWhenLoggedOut(vm, navController)
        toast(ctx, "after_ShowLoginWhenLoggedOut")
    }
}

private val Destination.shouldShowScaffoldElements get() = this !is LoginScreenDestination

@Composable
private fun ShowLoginWhenLoggedOut(
    vm: MainViewModel,
    navController: NavHostController
) {
    val ctx = LocalContext.current
    toast(ctx, "ShowLoginWhenLoggedOut")
    val currentDestination by navController.appCurrentDestinationAsState()
    val isLoggedIn by vm.isLoggedInFlow.collectAsState()
    toast(ctx, "isLoggedIn = $isLoggedIn")
    if (!isLoggedIn && currentDestination != LoginScreenDestination) {
        toast(ctx, "indien niet ingelogd en niet in de loginscreenDestination")
        // everytime destination changes or logged in state we check
        // if we have to show Login screen and navigate to it if so
        navController.navigate(LoginScreenDestination) {
            launchSingleTop = true
        }
    }
}