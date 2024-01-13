package com.example.presentation.ui

import android.content.ClipData
import android.content.ClipboardManager
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
            val (isClipboardClicked, onClipboardCopied) = remember { mutableStateOf("") }

            Android_KCSTheme {
                MainScreen(onClipboardCopied)
            }

            if (isClipboardClicked.isNotEmpty()) {
                copyToClipboard(isClipboardClicked)
            }
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard: ClipboardManager = this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.store_number), text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            Toast.makeText(this,
                getString(R.string.copy_to_clipboard_description), Toast.LENGTH_SHORT).show()
        }
    }
}