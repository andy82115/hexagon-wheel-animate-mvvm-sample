package com.example.moviefiltersample.repository

import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.MovieDetail
import com.example.moviefiltersample.api.data.NowPlaying
import com.example.moviefiltersample.api.data.SearchKeyword
import com.example.moviefiltersample.api.data.SearchMovieList
import com.example.moviefiltersample.api.data.SearchMovieParam
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getSearchKeyword(query: String): Flow<SearchKeyword>

    fun getSearchMovieList(param: SearchMovieParam, page: Int): Flow<SearchMovieList>

    fun getAllGenres(): Flow<Genres>

    fun getAllCountry(): Flow<List<Country>>

    fun getMovieDetail(movieId: Int): Flow<MovieDetail>

    fun getNowPlayingMovie(page: Int): Flow<NowPlaying>
}