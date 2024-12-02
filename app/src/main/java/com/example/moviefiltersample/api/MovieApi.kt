package com.example.moviefiltersample.api

import com.example.moviefiltersample.api.data.Country
import com.example.moviefiltersample.api.data.Genres
import com.example.moviefiltersample.api.data.MovieDetail
import com.example.moviefiltersample.api.data.NowPlaying
import com.example.moviefiltersample.api.data.SearchKeyword
import com.example.moviefiltersample.api.data.SearchMovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/discover/movie")
    suspend fun searchMusicWithFilter(
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("language") language: String? = "en-US",
        @Query("page") page: Int = 1,
        @Query("sort_by") sortBy: String? = "popularity.desc",
        @Query("with_genres") withGenres: Int? = 28,
        @Query("region") region: String? = "US",
        @Query("with_keywords") withKeywords: Int? = 1701,
        @Query("primary_release_year") year: Int? = 2024
    ): SearchMovieList

    @GET("3/genre/movie/list")
    suspend fun getAllGenres(
        @Query("language") language: String = "en",
    ): Genres

    @GET("3/configuration/countries")
    suspend fun getAllCountry(
        @Query("language") language: String = "en-US",
    ): List<Country>

    @GET("3/search/keyword")
    suspend fun searchKeyword(
        @Query("query") query: String = "hero",
    ): SearchKeyword

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int = 533535,
        @Query("language") language: String = "en-US",
    ): MovieDetail

    @GET("3/movie/popular")
    suspend fun getNowPlayingMovie(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): NowPlaying
}