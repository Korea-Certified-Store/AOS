package com.example.presentation.ui.map.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.presentation.R
import com.example.presentation.model.StoreDetail
import com.example.presentation.ui.map.summary.StoreTypeChips
import com.example.presentation.ui.theme.MediumBlue
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.util.MainConstants
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun StoreListItem(
    storeInfo: StoreDetail
) {
    BoxWithConstraints {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = DEFAULT_MARGIN.dp, vertical = DEFAULT_MARGIN.dp)
                .fillMaxWidth(1f)
                .wrapContentHeight(),
            constraintSet = setBottomSheetConstraints()
        ) {
            StoreTitle(storeInfo.displayName, "storeTitle")
            StorePrimaryTypeText(storeInfo.primaryTypeDisplayName ?: "상점", "storePrimaryType")
            StoreTypeChips(storeInfo.certificationName, "chips")
            StoreImageCard("storeImage", storeInfo.localPhotos)
        }
    }
}

fun setBottomSheetConstraints(): ConstraintSet {
    return ConstraintSet {
        val storeTitle = createRefFor("storeTitle")
        val storePrimaryType = createRefFor("storePrimaryType")
        val chips = createRefFor("chips")
        val storeImage = createRefFor("storeImage")

        constrain(storeTitle) {
            top.linkTo(parent.top)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storePrimaryType) {
            top.linkTo(storeTitle.bottom, 7.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(chips) {
            linkTo(top = storePrimaryType.bottom, bottom = parent.bottom, topMargin = 11.dp)
            linkTo(start = parent.start, end = storeImage.start, endMargin = 8.dp, bias = 0F)
            width = Dimension.fillToConstraints
        }
        constrain(storeImage) {
            end.linkTo(parent.end)
            linkTo(top = parent.top, bottom = parent.bottom, bias = 0F)
        }
    }
}

@Composable
fun StoreTitle(storeName: String, id: String) {
    Text(
        text = storeName,
        color = MediumBlue,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .layoutId(id)
    )
}

@Composable
fun StorePrimaryTypeText(storeType: String, id: String) {
    Text(
        text = storeType,
        color = MediumGray,
        fontSize = 9.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier
            .layoutId(id)
    )
}

@Composable
fun StoreImageCard(id: String, localPhotos: List<String>) {
    Card(
        modifier = Modifier
            .size(MainConstants.BOTTOM_SHEET_STORE_LIST_IMG_SIZE.dp)
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