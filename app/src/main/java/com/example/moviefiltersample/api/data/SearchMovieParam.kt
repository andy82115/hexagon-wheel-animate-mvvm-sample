package com.example.moviefiltersample.api.data

data class SearchMovieParam (
    val keyWordId: Int?,
    val genresId: Int?,
    val sortBy: String?,
    val includeAdult: Boolean,
    val region: String?,
    val year: Int?,
)