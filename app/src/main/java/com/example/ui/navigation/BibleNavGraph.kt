package com.example.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.AccessibilitySheet
import com.example.ui.components.BibleBottomBar
import com.example.ui.screens.BadgesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ParentalScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StoryReaderScreen
import com.example.ui.theme.ParchmentCream
import com.example.ui.viewmodel.MainUiState
import com.example.ui.viewmodel.MainViewModel

@Composable
fun BibleNavGraph(
    uiState: MainUiState,
    viewModel: MainViewModel
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: "splash"
    var showGlobalAccessibility by remember { mutableStateOf(false) }

    val bottomRoutes = setOf("home", "badges", "dashboard", "parents")
    val showBottomBar = currentRoute in bottomRoutes

    Scaffold(
        containerColor = ParchmentCream,
        bottomBar = {
            if (showBottomBar) {
                BibleBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { target ->
                        navController.navigate(target) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                fadeIn(tween(260)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(280))
            },
            exitTransition = {
                fadeOut(tween(220)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(260))
            },
            popEnterTransition = {
                fadeIn(tween(260)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(280))
            },
            popExitTransition = {
                fadeOut(tween(220)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(260))
            }
        ) {
            composable("splash") {
                SplashScreen(
                    childName = uiState.preferences.childName,
                    storiesLoaded = !uiState.isLoading,
                    onContinue = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    uiState = uiState,
                    onSelectTestament = viewModel::selectTestament,
                    onSearchChange = viewModel::updateSearchQuery,
                    onOpenStory = { id -> navController.navigate("reader/$id") }
                )
            }

            composable(
                route = "reader/{storyId}",
                arguments = listOf(navArgument("storyId") { type = NavType.IntType })
            ) { entry ->
                val storyId = entry.arguments?.getInt("storyId") ?: 1
                val story = uiState.stories.find { it.id == storyId }
                if (story == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (uiState.isLoading) CircularProgressIndicator() else Text("Histoire introuvable")
                    }
                } else {
                    StoryReaderScreen(
                        story = story,
                        preferences = uiState.preferences,
                        ttsManager = viewModel.ttsManager,
                        onUpdateAccessibility = viewModel::updateAccessibility,
                        onBack = { navController.popBackStack() },
                        onStartQuiz = { id ->
                            viewModel.clearStreakBanner()
                            navController.navigate("quiz/$id")
                        }
                    )
                }
            }

            composable(
                route = "quiz/{storyId}",
                arguments = listOf(navArgument("storyId") { type = NavType.IntType })
            ) { entry ->
                val storyId = entry.arguments?.getInt("storyId") ?: 1
                val story = uiState.stories.find { it.id == storyId }
                if (story != null) {
                    QuizScreen(
                        story = story,
                        difficulty = uiState.preferences.difficulty,
                        streakReward = uiState.lastStreakReward,
                        onQuizFinished = { score, total ->
                            viewModel.recordQuizAndReadingCompletion(story, score, total)
                        },
                        onDoneNavigateHome = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        onBackToStory = { navController.popBackStack() }
                    )
                }
            }

            composable("badges") {
                BadgesScreen(badges = uiState.badges)
            }

            composable("dashboard") {
                DashboardScreen(
                    uiState = uiState,
                    onSaveProfile = viewModel::updateChildProfile,
                    onOpenAccessibility = { showGlobalAccessibility = true }
                )
            }

            composable("parents") {
                ParentalScreen(
                    uiState = uiState,
                    onUpdateParentalSettings = viewModel::updateParentalSettings,
                    onOpenAccessibility = { showGlobalAccessibility = true },
                    onResetProgress = viewModel::resetAllProgress
                )
            }
        }
    }

    if (showGlobalAccessibility) {
        AccessibilitySheet(
            preferences = uiState.preferences,
            onUpdateAccessibility = viewModel::updateAccessibility,
            onTestFrenchVoice = {
                viewModel.ttsManager.speakSample(
                    uiState.preferences.ttsSpeechRate,
                    uiState.preferences.ttsPitch
                )
            },
            onDismiss = { showGlobalAccessibility = false }
        )
    }
}
