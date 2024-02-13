package com.example.presentation.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.domain.model.search.SearchWord
import com.example.presentation.R
import com.example.presentation.ui.map.list.StoreListDivider
import com.example.presentation.ui.navigation.Screen
import com.example.presentation.ui.theme.Black
import com.example.presentation.ui.theme.SemiLightGray
import com.example.presentation.ui.theme.White
import com.example.presentation.util.MainConstants.DEFAULT_MARGIN
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_HEIGHT
import com.example.presentation.util.MainConstants.SEARCH_TEXT_FIELD_TOP_PADDING

@Composable
fun SearchScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchTextField(navController)
        RecentSearchList()
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchTextField(
    navController: NavHostController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    var text by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = text,
        onValueChange = { textValue -> text = textValue },
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(
                        start = DEFAULT_MARGIN.dp,
                        end = DEFAULT_MARGIN.dp,
                        top = SEARCH_TEXT_FIELD_TOP_PADDING.dp,
                        bottom = 8.dp
                    )
                    .shadow(4.dp, RoundedCornerShape(size = 12.dp))
                    .fillMaxWidth()
                    .height(SEARCH_TEXT_FIELD_HEIGHT.dp)
                    .background(color = White, shape = RoundedCornerShape(size = 12.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Spacer(modifier = Modifier.width(width = DEFAULT_MARGIN.dp))
                    if (text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_placeholder_text),
                            fontSize = 14.sp,
                            color = SemiLightGray,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(width = 8.dp))
                }
                Row {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search),
                        contentDescription = "Search",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(width = DEFAULT_MARGIN.dp))
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        textStyle = TextStyle(color = Black, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        modifier = Modifier.focusRequester(focusRequester),
        keyboardActions = KeyboardActions(onDone = {
            insertSearchWord(text, viewModel)

            navController.currentBackStackEntry?.savedStateHandle?.set(
                key = "search_text",
                value = text
            )
            navController.navigate(Screen.Main.route)
            keyboardController?.hide()
        })
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

fun insertSearchWord(keyword: String, viewModel: SearchViewModel) {
    val nowTime = System.currentTimeMillis()
    viewModel.insertSearchWord(SearchWord(keyword = keyword, searchTime = nowTime))
}

@Preview
@Composable
fun RecentSearchList(viewModel: SearchViewModel = hiltViewModel()) {
    viewModel.getRecentSearchWord()
    val recentSearchWords by viewModel.recentSearchWords.collectAsStateWithLifecycle()

    LazyColumn {
        itemsIndexed(recentSearchWords) { _, item ->
            RecentSearchItem(item)
            StoreListDivider()
        }
    }
}

@Composable
fun RecentSearchItem(searchWord: SearchWord, viewModel: SearchViewModel = hiltViewModel()) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = DEFAULT_MARGIN.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = searchWord.keyword)
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.delete),
            contentDescription = "delete",
            modifier = Modifier.clickable {
                viewModel.deleteSearchWordById(searchWord.id)
            }
        )
    }
}