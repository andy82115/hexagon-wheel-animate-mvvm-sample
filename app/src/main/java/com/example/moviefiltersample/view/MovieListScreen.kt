package com.example.moviefiltersample.view

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviefiltersample.api.data.SearchMovieParam
import com.example.moviefiltersample.component.ResultCardItem
import com.example.moviefiltersample.viewmodels.MovieListViewModel
import com.example.moviefiltersample.viewmodels.MovieResultViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onResultItemClick: (Int) -> Unit = {}
) {
    val data = viewModel.suggestMovieList


    val page = rememberSaveable { mutableIntStateOf(1) }

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
                    loadData(page, viewModel)
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

private suspend fun loadData(page: MutableState<Int>, viewModel: MovieListViewModel) {
    viewModel.dataOnLoadMore()
    viewModel.requestNowPlayingMovie(page.value).collect { value ->
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