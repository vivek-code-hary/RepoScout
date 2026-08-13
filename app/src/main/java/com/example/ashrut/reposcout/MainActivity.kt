package com.example.ashrut.reposcout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ashrut.reposcout.presentation.explore.ExploreScreen
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModel
import com.example.ashrut.reposcout.presentation.explore.ExploreViewModelFactory
import com.example.ashrut.reposcout.ui.theme.RepoScoutTheme
import com.example.ashrut.reposcout.utils.AppContainer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val factory = ExploreViewModelFactory(
                repository = AppContainer.gitHubRepository
            )
            val viewModel: ExploreViewModel = viewModel(factory = factory)
            RepoScoutTheme {
                ExploreScreen(viewModel = viewModel)
            }
        }
    }
}

