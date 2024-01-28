package com.example.presentation.ui.map.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.model.StoreType
import com.example.presentation.ui.map.MapViewModel
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.Blue
import com.example.presentation.ui.theme.White

@Composable
fun FilterComponent(
    isKindFilterClicked: Boolean,
    onKindFilterChanged: (Boolean) -> Unit,
    isGreatFilterClicked: Boolean,
    onGreatFilterChanged: (Boolean) -> Unit,
    isSafeFilterClicked: Boolean,
    onSafeFilterChanged: (Boolean) -> Unit,
    mapViewModel: MapViewModel,
    onFilterStateChanged: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(top = 12.dp, start = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        FilterButton(
            storeType = StoreType.KIND,
            isKindFilterClicked,
            onKindFilterChanged,
            mapViewModel,
            onFilterStateChanged
        )
        FilterButton(
            storeType = StoreType.GREAT,
            isGreatFilterClicked,
            onGreatFilterChanged,
            mapViewModel,
            onFilterStateChanged
        )
        FilterButton(
            storeType = StoreType.SAFE,
            isSafeFilterClicked,
            onSafeFilterChanged,
            mapViewModel,
            onFilterStateChanged
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButton(
    storeType: StoreType,
    isFilterClicked: Boolean,
    onFilterChanged: (Boolean) -> Unit,
    mapViewModel: MapViewModel,
    onFilterStateChanged: (Boolean) -> Unit
) {
    val certificationName = stringResource(id = storeType.storeTypeName).replace(" ", "")
    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
        Button(
            onClick = {
                mapViewModel.updateFilterSet(certificationName, isFilterClicked.not())
                onFilterChanged(isFilterClicked.not())
                onFilterStateChanged(true)
            },
            modifier = Modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                .padding(end = 6.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 11.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFilterClicked) Blue else White,
                contentColor = if (isFilterClicked) White else Black
            ),
            shape = RoundedCornerShape(30.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            FilterCircle(storeType.color)
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = stringResource(storeType.storeTypeName),
                fontSize = 12.sp,
                fontWeight = FontWeight.Thin
            )
        }
    }
}

@Composable
fun FilterCircle(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color = color, shape = CircleShape),
    )
}