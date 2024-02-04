package com.example.presentation.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.presentation.R
import com.example.presentation.ui.theme.SemiLightGray
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun StoreImageCard(localPhotos: List<String>, size: Int, id: String) {
    Card(
        modifier = Modifier
            .size(size.dp)
            .layoutId(id),
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(0.3.dp, SemiLightGray)
    ) {
        StoreImage(localPhotos = localPhotos)
    }
}

@Composable
fun StoreImage(localPhotos: List<String>) {
    if (localPhotos.isNotEmpty()) {
        CoilImage(
            imageModel = localPhotos.first(),
            contentScale = ContentScale.Crop,
            placeHolder = painterResource(R.drawable.empty_store_img),
            error = painterResource(R.drawable.empty_store_img)
        )
    } else {
        Image(
            painter = painterResource(R.drawable.empty_store_img),
            contentDescription = "Store Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(1f)
        )
    }
}