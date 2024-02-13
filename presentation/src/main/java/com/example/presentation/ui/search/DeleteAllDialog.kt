package com.example.presentation.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.White

@Composable
fun DeleteAllDialog(onDeleteAllDialogVisibleChanged: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { onDeleteAllDialogVisibleChanged(false) },
        title = {
            Text(
                text = stringResource(R.string.delete),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black
            )
        },
        text = {
            Text(
                text = stringResource(R.string.do_you_want_delete_all),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Black
            )
        },
        dismissButton = {
            Text(
                text = stringResource(R.string.cancel),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Black,
                modifier = Modifier.clickable { onDeleteAllDialogVisibleChanged(false) }
            )
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.delete2),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Black
            )
        },
        shape = RectangleShape,
        containerColor = White
    )
}