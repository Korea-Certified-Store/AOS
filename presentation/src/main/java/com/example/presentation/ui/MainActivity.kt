package com.example.presentation.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.presentation.R
import com.example.presentation.ui.theme.Android_KCSTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalNaverMapApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val (clipboardStoreNumber, onClipboardChanged) = remember { mutableStateOf("") }
            val (callStoreNumber, onCallStoreChanged) = remember { mutableStateOf("") }

            Android_KCSTheme {
                MainScreen(onClipboardChanged, onCallStoreChanged)
            }

            if (clipboardStoreNumber.isNotEmpty()) {
                copyToClipboard(clipboardStoreNumber)
                onClipboardChanged("")
            }

            if (callStoreNumber.isNotEmpty()) {
                callStore(callStoreNumber)
                onCallStoreChanged("")
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard: ClipboardManager =
            this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.store_number), text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            Toast.makeText(
                this,
                getString(R.string.copy_to_clipboard_description), Toast.LENGTH_SHORT
            ).show()
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