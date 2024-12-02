package com.example.moviefiltersample.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.SearchMovieParam
import com.example.moviefiltersample.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortType {
    popularity, revenue, title, vote_average, vote_count
}

enum class SettingAnswerType {
    NotAllow, Allow
}

const val ImageLinkPrefix = "https://image.tmdb.org/t/p/w600_and_h900_bestv2"

@HiltViewModel
open class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _allGenresList = MutableStateFlow<Genres?>(null)
    val allGenresList: StateFlow<Genres?> = _allGenresList

    private val _allCountryList = MutableStateFlow<List<Country>>(emptyList())
    val allCountryList: StateFlow<List<Country>> = _allCountryList

    private val _shareSearchMovieData = mutableStateOf(SearchMovieParam(
        keyWordId = null,
        genresId = 28,
        sortBy = "popularity.desc",
        includeAdult = false,
        region = "US",
        year = 2024
    ))
    val shareSearchMovieData: State<SearchMovieParam> = _shareSearchMovieData

    fun setShareSearchMovieData(data: SearchMovieParam) {
        _shareSearchMovieData.value = data
    }

    fun getAllGenresList(): Flow<Genres?> {
        if (allGenresList.value != null) return allGenresList
        viewModelScope.launch {
            try {
                val response = repository.getAllGenres()
                _allGenresList.value = response.singleOrNull()
            } catch (e: Exception) {
                Log.e("Error", "getAllGenresList: $e")
            }
        }
        return allGenresList
    }

    fun getAllCountryList(): Flow<List<Country>> {
        if (allCountryList.value.isNotEmpty()) return allCountryList
        viewModelScope.launch {
            try {
                val response = repository.getAllCountry()
                _allCountryList.value = response.map {
                    it.filter { item ->
                        item.nativeName.contains("Unit")
                    }
                }.single()
            } catch (e: Exception) {
                Log.e("Error", "getAllCountries: $e")
            }
        }
        return allCountryList
    }
}