package com.example.moviefiltersample.api.data


import com.google.gson.annotations.SerializedName

data class Genres(
    @SerializedName("genres")
    val genres: List<Genre>
) {
    data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}