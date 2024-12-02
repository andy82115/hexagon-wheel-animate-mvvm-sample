package com.example.moviefiltersample.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.KeyWordResult
import com.example.moviefiltersample.api.data.SearchKeyword
import com.example.moviefiltersample.api.data.SearchMovieResult
import com.example.moviefiltersample.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
open class MovieFilterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    //List data
    private val _yearList = mutableStateOf((2000..2024).map { it.toString() })
    val yearList: State<List<String>> = _yearList

    private val _countryList = mutableStateOf(listOf<Country>())
    val countryList: State<List<Country>> = _countryList

    private val _genres = mutableStateOf<Genres?>(null)
    val genres: State<Genres?> = _genres

    private val _sortTypes = mutableStateOf(SortType.entries)
    val sortTypes: State<List<SortType>> = _sortTypes

    private val _keywordResult = mutableStateOf<SearchKeyword?>(null)
    val keywordResult: State<SearchKeyword?> = _keywordResult

    //Selected Data
    private val _keywordText = mutableStateOf("")
    val keywordText: State<String> = _keywordText

    private val _keywordSelected = mutableStateOf<KeyWordResult?>(null)
    val keywordSelected: State<KeyWordResult?> = _keywordSelected

    private val _yearText = mutableStateOf(yearList.value.last())
    val yearText: State<String> = _yearText

    private val _country = mutableStateOf<Country?>(null)
    val country: State<Country?> = _country

    private val _genre = mutableStateOf<Genres.Genre?>(null)
    val genre: State<Genres.Genre?> = _genre

    private val _sortType = mutableStateOf(SortType.entries.first())
    val sortType: State<SortType> = _sortType

    private val _adultType = mutableStateOf(SettingAnswerType.Allow)
    val adultType: State<SettingAnswerType> = _adultType

    //data setting
    fun setCountryList(countries: List<Country>){
        _countryList.value = countries
    }

    fun setCountry(country: Country?){
        _country.value = country
    }

    fun setGenreList(genres: Genres?){
        _genres.value = genres
    }

    fun setGenre(genre: Genres.Genre){
        _genre.value = genre
    }

    fun setSearchKeyword(keywordResult: SearchKeyword?){
        _keywordResult.value = keywordResult
    }

    fun setSearchKeywordSelected(keywordResult: KeyWordResult?){
        _keywordSelected.value = keywordResult
    }

    fun setSearchKeywordText(keywordText: String){
        _keywordText.value = keywordText
    }

    fun setYearText(yearText: String){
        _yearText.value = yearText
    }

    fun setSortType(sortType: SortType){
        _sortType.value = sortType
    }

    fun setAdultType(adultType: SettingAnswerType){
        _adultType.value = adultType
    }

    //Repository function
    fun getSearchKeyword(query: String): StateFlow<SearchKeyword?> {
        return repository.getSearchKeyword(query)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null
            )
    }
}