package com.example.presentation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.presentation.R
import com.example.presentation.ui.theme.Android_KCSTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalNaverMapApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (callStoreNumber, onCallStoreChanged) = remember { mutableStateOf("") }

            val mainViewModel by viewModels<MainViewModel>()

            Android_KCSTheme {
                MainScreen(
                    mainViewModel,
                    onCallStoreChanged
                )
            }

            if (callStoreNumber.isNotEmpty()) {
                callStore(callStoreNumber)
                onCallStoreChanged("")
            }
        }
    }

    private fun callStore(storeNumber: String) {
        startActivity(
            Intent(
                "android.intent.action.DIAL",
                Uri.parse(String.format(resources.getString(R.string.tel), storeNumber))
            )
        )
    }
}