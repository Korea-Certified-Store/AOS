package com.example.presentation.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.R
import com.example.presentation.ui.theme.MediumGray

@Composable
fun EmptyScreen(@StringRes description: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 141.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .size(40.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.exclamation_mark),
            contentDescription = "exclamation mark"
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp),
            text = stringResource(description),
            color = MediumGray,
            fontSize = 14.sp,
            fontWeight = Medium,
        )
    }
}