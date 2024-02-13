package com.example.presentation.ui.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.presentation.R
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.DarkGray
import com.example.presentation.ui.theme.LightGray
import com.example.presentation.ui.theme.MediumGray
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_HEIGHT
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_TOP_PADDING


@Composable
fun SearchScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SearchAppBar(navController)
        SearchDivider(6)
        RecentSearchList()
    }
}

@Composable
private fun SearchAppBar(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = DEFAULT_MARGIN.dp,
                vertical = SEARCH_TEXT_FIELD_TOP_PADDING.dp
            )
    ) {
        BackArrow(navController)
        SearchTextField(navController)
    }
}

@Composable
fun SearchDivider(thickness: Int) {
    Divider(
        modifier = Modifier
            .fillMaxWidth(),
        thickness = thickness.dp, color = LightGray
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = searchText,
        onValueChange = { textValue -> searchText = textValue },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(
                        start = DEFAULT_MARGIN.dp,
                    )
                    .fillMaxWidth()
                    .height(SEARCH_TEXT_FIELD_HEIGHT.dp)
                    .background(color = White, shape = RoundedCornerShape(size = 12.dp))
                    .border(
                        border = BorderStroke(1.5.dp, MediumGray),
                        shape = RoundedCornerShape(size = 12.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Spacer(modifier = Modifier.width(width = DEFAULT_MARGIN.dp))
                    if (searchText.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_placeholder_text),
                            fontSize = 14.sp,
                            color = SemiLightGray,
                            fontWeight = Medium
                        )
                    } else {
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(width = 8.dp))
                }
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = if (searchText.isEmpty()) R.drawable.search else R.drawable.delete),
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable(enabled = searchText.isNotEmpty()) {
                                searchText = ""
                            },
                        colorFilter = ColorFilter.tint(DarkGray)
                    )
                    Spacer(modifier = Modifier.width(width = DEFAULT_MARGIN.dp))
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(color = Black, fontSize = 14.sp, fontWeight = Medium),
        modifier = Modifier.focusRequester(focusRequester),
        keyboardActions = KeyboardActions(onDone = {
            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "search_text",
                value = searchText
            )
            navController.navigate(Screen.Main.route)
            keyboardController?.hide()
        })
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun BackArrow(navController: NavHostController) {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.arrow),
        contentDescription = "Arrow",
        modifier = Modifier
            .width(10.dp)
            .height(18.dp)
            .clickable {
                navController.popBackStack()
            }
    )
}

@Composable
fun RecentSearchList() {
    val exampleItems1 = emptyList<String>()
    val exampleItems2 = listOf(
        "검색어1",
        "검색어2검색어2검색어2검색어2",
        "검색어5검색어5검색어5검색어5검색어5검색어5검색어5검색어5검색어5"
    )
    val recentSearchItems = exampleItems2

    TitleText(recentSearchItems)
    SearchDivider(1)
    if (recentSearchItems.isEmpty()) {
        EmptyRecentSearchScreen()
    } else {
        LazyColumn {
            itemsIndexed(recentSearchItems) { idx, item ->
                RecentSearchItem(text = item)
                SearchDivider(1)
            }
        }
    }
}

@Composable
private fun EmptyRecentSearchScreen() {
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
            text = "최근 검색 기록이 없습니다",
            color = MediumGray,
            fontSize = 14.sp,
            fontWeight = Medium,
        )
    }
}

@Composable
fun TitleText(exampleItems: List<String>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = DEFAULT_MARGIN.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.recent_search_word),
            color = Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
        )
        if (exampleItems.isNotEmpty()) {
            Text(
                text = stringResource(R.string.delete_all),
                color = MediumGray,
                fontSize = 12.sp,
                fontWeight = Medium,
            )
        }
    }
}

@Composable
fun RecentSearchItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(53.dp)
            .padding(horizontal = DEFAULT_MARGIN.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentSize(),
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.search_blue),
                contentDescription = "search blue",
                modifier = Modifier.size(15.dp)
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(start = 11.dp),
                text = text,
                color = Black,
                fontSize = 16.sp,
                fontWeight = Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.delete_circle),
            contentDescription = "delete",
            modifier = Modifier.size(16.dp)
        )
    }
}