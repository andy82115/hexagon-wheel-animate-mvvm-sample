package com.example.moviefiltersample.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.moviefiltersample.R
import com.example.moviefiltersample.api.data.SearchMovieParam
import com.example.moviefiltersample.component.ResultCardItem
import com.example.moviefiltersample.viewmodels.ImageLinkPrefix
import com.example.moviefiltersample.viewmodels.MainViewModel
import com.example.moviefiltersample.viewmodels.MovieResultViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun FilterResultScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    viewModel: MovieResultViewModel = hiltViewModel(),
    onResultItemClick: (Int) -> Unit = {}
) {
    val data = viewModel.searchMovieList


    val page = rememberSaveable { mutableIntStateOf(1) }
    val param = mainViewModel.shareSearchMovieData.value

    val threshold = 1
    val listState = rememberLazyListState()
    val loadMore = remember(
        listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
    ) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            viewModel.isLoadMoreAllow.value && lastVisibleItemIndex > (totalItems - threshold)
        }
    }

    LaunchedEffect (loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect{
                if (it) {
                    loadData(param, page, viewModel)
                }
            }
    }

    LazyColumn(state = listState) {

        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        items (
            count = data.size,
            itemContent = { index ->
                ResultCardItem(
                    movieId = data[index].id,
                    imgUrl = data[index].posterPath,
                    title = data[index].title,
                    overview = data[index].overview,
                    onResultItemClick = onResultItemClick
                )
            }
        )

        if (viewModel.isMoreDataLoading.value) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

private suspend fun loadData(param: SearchMovieParam, page: MutableState<Int>, viewModel: MovieResultViewModel) {
    viewModel.dataOnLoadMore()
    viewModel.requestSearchedMovie(param, page.value).collect { value ->
        if (value != null) {
            viewModel.addToMovieList(value.results)
            if (value.page == value.totalPages) {
                viewModel.disableLoadMore()
            }
            page.value += 1
        }
        viewModel.dataOnLoadMoreComplete()
    }
}

@Preview
@Composable
fun FilterResultScreenPreview() {
    FilterResultScreen()
}