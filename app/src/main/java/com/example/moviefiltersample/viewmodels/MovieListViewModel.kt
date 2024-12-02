package com.example.moviefiltersample.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefiltersample.api.data.MovieListResult
import com.example.moviefiltersample.api.data.NowPlaying
import com.example.moviefiltersample.api.data.SearchMovieList
import com.example.moviefiltersample.api.data.SearchMovieParam
import com.example.moviefiltersample.api.data.SearchMovieResult
import com.example.moviefiltersample.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MovieListViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _suggestMovieList = mutableStateListOf<MovieListResult>()
    val suggestMovieList: List<MovieListResult> = _suggestMovieList

    private val _isMoreDataLoading = mutableStateOf(false)
    val isMoreDataLoading: State<Boolean> = _isMoreDataLoading

    private val _isLoadMoreAllow = mutableStateOf(true)
    val isLoadMoreAllow: State<Boolean> = _isLoadMoreAllow

    fun disableLoadMore(){
        _isLoadMoreAllow.value = false
    }

    fun dataOnLoadMore(){
        _isMoreDataLoading.value = true
    }

    fun dataOnLoadMoreComplete(){
        viewModelScope.launch {
            delay(500)
            _isMoreDataLoading.value = false
        }
    }

    fun addToMovieList(data: List<MovieListResult>){
        _suggestMovieList.addAll(data)
    }

    fun requestNowPlayingMovie(page: Int): StateFlow<NowPlaying?> {
        return repository.getNowPlayingMovie(page)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }
}