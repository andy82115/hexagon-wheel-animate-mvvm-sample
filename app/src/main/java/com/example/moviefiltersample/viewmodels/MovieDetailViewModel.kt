package com.example.moviefiltersample.viewmodels

import com.example.moviefiltersample.api.data.MovieDetail
import kotlinx.coroutines.flow.collect
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class MovieDetailViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _movieDetail = mutableStateOf<MovieDetail?>(null)
    val searchMovieList: State<MovieDetail?> = _movieDetail

    fun updateMovieDetail(movieId: Int){
        viewModelScope.launch {
            getMovieDetail(movieId).collect{ value ->
                _movieDetail.value = value
            }
        }
    }

    private fun getMovieDetail(movieId: Int): StateFlow<MovieDetail?> {
        return repository.getMovieDetail(movieId)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }
}