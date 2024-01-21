package com.example.presentation.ui

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.presentation.R
import com.example.presentation.model.Contact
import com.example.presentation.ui.map.LocationPermissionRequest
import com.example.presentation.ui.theme.Android_KCSTheme
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalNaverMapApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel by viewModels<MainViewModel>()

        setContent {
            LocationPermissionRequest(mainViewModel)

            val (callStoreNumber, onCallStoreChanged) = remember { mutableStateOf("") }
            val (contactInfo, onSaveStoreNumberChanged) = remember {
                mutableStateOf(INIT_CONTACT_INFO)
            }
            val (clipboardStoreNumber, onClipboardChanged) = remember { mutableStateOf("") }

            Android_KCSTheme {
                MainScreen(
                    mainViewModel,
                    onCallStoreChanged,
                    onSaveStoreNumberChanged,
                    onClipboardChanged
                )
            }

            if (callStoreNumber.isNotEmpty()) {
                callStore(callStoreNumber)
                onCallStoreChanged("")
            }

            if (contactInfo.name.isNotEmpty()) {
                saveStoreNumber(contactInfo)
                onSaveStoreNumberChanged(INIT_CONTACT_INFO)
            }

            if (clipboardStoreNumber.isNotEmpty()) {
                copyToClipboard(clipboardStoreNumber)
                onClipboardChanged("")
            }
        }

        if (isLocationPermissionGranted(this)) {
            lifecycleScope.launch {
                mainViewModel.updateLocationPermission(true)
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

    private fun saveStoreNumber(contactInfo: Contact) {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE
        }

        intent.apply {
            putExtra(ContactsContract.Intents.Insert.NAME, contactInfo.name)
            putExtra(ContactsContract.Intents.Insert.PHONE, contactInfo.phone)
            putExtra(ContactsContract.Intents.Insert.POSTAL, contactInfo.address)
        }

        startActivity(intent)
    }

    private fun copyToClipboard(text: String) {
        val clipboard: ClipboardManager =
            this.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.store_number), text)
        clipboard.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            Toast.makeText(
                this,
                getString(R.string.copy_to_clipboard_description), Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun isLocationPermissionGranted(context: Context) =
        !(ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED)

    companion object {
        val INIT_CONTACT_INFO = Contact("", "", "")
    }
}