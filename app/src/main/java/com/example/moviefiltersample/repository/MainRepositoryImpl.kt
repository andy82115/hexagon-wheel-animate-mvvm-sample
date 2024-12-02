package com.example.moviefiltersample.repository

import android.util.Log
import com.example.moviefiltersample.api.MovieApi
import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.MovieDetail
import com.example.moviefiltersample.api.data.NowPlaying
import com.example.moviefiltersample.api.data.SearchKeyword
import com.example.moviefiltersample.api.data.SearchMovieList
import com.example.moviefiltersample.api.data.SearchMovieParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi
) : MainRepository {
    override fun getSearchKeyword(query: String): Flow<SearchKeyword> = flow {
        val list = movieApi.searchKeyword(query)
        Log.d("SearchedMove", list.toString())
        emit(list)
    }

    override fun getSearchMovieList(param: SearchMovieParam, page: Int): Flow<SearchMovieList> = flow {
        val response = movieApi.searchMusicWithFilter(
            includeAdult = param.includeAdult,
            sortBy = param.sortBy,
            withGenres = param.genresId,
            region = param.region,
            withKeywords = param.keyWordId,
            year = param.year,
            page = page,
        )
        Log.d("SearchedMovie", response.toString())
        emit(response)
    }

    override fun getAllGenres(): Flow<Genres> = flow {
        val response = movieApi.getAllGenres()
        Log.d("Genres", response.toString())
        emit(response)
    }

    override fun getAllCountry(): Flow<List<Country>> = flow {
        val response = movieApi.getAllCountry()
        Log.d("Country", response.toString())
        emit(response)
    }

    override fun getMovieDetail(movieId: Int): Flow<MovieDetail> = flow {
        val response = movieApi.getMovieDetail(movieId)
        Log.d("MovieDetail", response.toString())
        emit(response)
    }

    override fun getNowPlayingMovie(page: Int): Flow<NowPlaying> = flow {
        val response = movieApi.getNowPlayingMovie(page = page)
        Log.d("NowPlaying", response.toString())
        emit(response)
    }
}