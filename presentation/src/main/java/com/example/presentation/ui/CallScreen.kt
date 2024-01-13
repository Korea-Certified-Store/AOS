package com.example.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White

@Composable
@Preview
fun StoreCallDialog() {
    Dialog(onDismissRequest = {}) {
        Surface(
            modifier = Modifier
                .width(282.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(3.dp),
            color = White
        ) {
            Column(modifier = Modifier.padding(horizontal = 44.dp, vertical = 14.dp)) {
                Text(
                    text = "0507-1369-4848",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 19.sp
                )
                Text(
                    text = "전화 걸기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                    )
                Text(
                    text = "전화번호에 저장하기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
                Text(
                    text = "클립보드에 복사하기",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp
                )
                Text(
                    text = "취소",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    fontWeight = FontWeight.Medium,
                    color = Blue,
                    textAlign = TextAlign.End,
                    fontSize = 12.sp
                )
            }
        }
    }
}