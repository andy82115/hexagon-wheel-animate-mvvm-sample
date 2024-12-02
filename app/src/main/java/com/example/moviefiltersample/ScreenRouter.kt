package com.example.moviefiltersample

interface ScreenRouter {
    val icon: Int
    val route: String
}

object RouterMovieFilter : ScreenRouter {
    override val icon = R.drawable.ic_movie_filter
    override val route = "movie_filter"
}

object RouterMovieList : ScreenRouter {
    override val icon = R.drawable.ic_movie_list
    override val route = "movie_list"
}

object RouterMovieResult : ScreenRouter {
    override val icon = R.drawable.ic_movie_filter
    override val route = "movie_result"
}

object RouterMovieDetail : ScreenRouter {
    val argKey = "movieId"
    override val icon = R.drawable.ic_movie_filter
    override val route = "movie_detail"
}

//val layer1Router = listOf(RouterMovieFilter, RouterMovieList)
val layer1Router = listOf(RouterMovieFilter, RouterMovieList)


