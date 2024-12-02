package com.example.moviefiltersample.api.data


import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("english_name")
    val englishName: String,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("native_name")
    val nativeName: String
)