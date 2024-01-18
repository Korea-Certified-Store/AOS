package com.example.presentation.ui.map

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.presentation.R
import com.example.presentation.model.Contact
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White

@Composable
fun StoreCallDialog(
    contactInfo: Contact,
    onCallDialogCanceled: (Boolean) -> Unit,
    onCallStoreChanged: (String) -> Unit,
    onSaveStoreNumberChanged: (Contact) -> Unit,
    onClipboardChanged: (String) -> Unit,
) {
    Dialog(onDismissRequest = { onCallDialogCanceled(true) }) {
        Surface(
            modifier = Modifier
                .width(282.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(3.dp),
            color = White
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.End
            ) {
                StoreNumberText(contactInfo.phone)
                CallOptionTextButton(
                    description = R.string.call_number,
                    contactInfo = contactInfo,
                    onCallStoreChanged = onCallStoreChanged
                )
                CallOptionTextButton(
                    description = R.string.save_number,
                    contactInfo = contactInfo,
                    onSaveStoreNumberChanged = onSaveStoreNumberChanged
                )
                CallOptionTextButton(
                    description = R.string.copy_to_clipboard,
                    contactInfo = contactInfo,
                    onClipboardChanged = onClipboardChanged
                )
                CallCancelTextButton(onCallDialogCanceled = onCallDialogCanceled)
            }
        }
    }
}

@Composable
fun StoreNumberText(number: String) {
    Text(
        text = number,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 15.dp, horizontal = 24.dp),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 19.sp
    )
}

@Composable
fun CallOptionTextButton(
    @StringRes description: Int,
    contactInfo: Contact,
    onCallStoreChanged: (String) -> Unit = {},
    onSaveStoreNumberChanged: (Contact) -> Unit = {},
    onClipboardChanged: (String) -> Unit = {}
) {
    TextButton(
        onClick = {
            when (description) {
                R.string.call_number -> onCallStoreChanged(contactInfo.phone)
                R.string.save_number -> onSaveStoreNumberChanged(contactInfo)
                R.string.copy_to_clipboard -> onClipboardChanged(contactInfo.phone)
            }
        },
        colors = ButtonDefaults.textButtonColors(contentColor = Black),
        shape = RectangleShape,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 15.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(description),
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CallCancelTextButton(onCallDialogCanceled: (Boolean) -> Unit) {
    TextButton(
        onClick = { onCallDialogCanceled(true) },
        colors = ButtonDefaults.textButtonColors(contentColor = Blue),
        shape = RectangleShape,
        modifier = Modifier,
        contentPadding = PaddingValues(horizontal = 10.dp),
    ) {
        Text(
            text = stringResource(R.string.cancel),
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        )
    }
}