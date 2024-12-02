package com.example.moviefiltersample.api.data


import com.google.gson.annotations.SerializedName

data class SearchKeyword(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val results: List<KeyWordResult>,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)

data class KeyWordResult(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)