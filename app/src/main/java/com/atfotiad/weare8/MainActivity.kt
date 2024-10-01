package com.atfotiad.weare8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.atfotiad.weare8.WeAre8Application.Companion.FeedApp
import com.atfotiad.weare8.ui.theme.WeAre8Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeAre8Theme {
                FeedApp()
            }
        }
    }
}

