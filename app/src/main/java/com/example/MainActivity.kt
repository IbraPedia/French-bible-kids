package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.BibleNavGraph
import com.example.ui.theme.BibleKidsTheme
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = viewModel()
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()

            BibleKidsTheme(
                dyslexiaFriendly = uiState.preferences.dyslexiaFriendly,
                fontScale = uiState.preferences.textSizeScale
            ) {
                BibleNavGraph(
                    uiState = uiState,
                    viewModel = mainViewModel
                )
            }
        }
    }
}
