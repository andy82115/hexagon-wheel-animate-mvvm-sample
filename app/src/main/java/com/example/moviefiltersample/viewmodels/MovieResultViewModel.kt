package com.example.moviefiltersample.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefiltersample.api.data.SearchKeyword
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
open class MovieResultViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _searchMovieList = mutableStateListOf<SearchMovieResult>()
    val searchMovieList: List<SearchMovieResult> = _searchMovieList

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

    fun addToMovieList(data: List<SearchMovieResult>){
        _searchMovieList.addAll(data)
    }

    fun requestSearchedMovie(param: SearchMovieParam, page: Int): StateFlow<SearchMovieList?> {
        return repository.getSearchMovieList(param, page)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }
}