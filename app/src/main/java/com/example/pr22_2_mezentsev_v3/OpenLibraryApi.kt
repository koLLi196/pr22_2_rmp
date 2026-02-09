package com.example.pr22_2_mezentsev_v3

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

interface OpenLibraryApi {
    @GET("isbn/{isbn}.json")
    fun getBookByIsbn(@Path("isbn") isbn: String): Call<OpenLibraryResponse>
}

data class OpenLibraryResponse(
    val title: String?,
    val authors: List<Author>?,
    val covers: List<Int>?,
    val description: Any?
) {
    data class Author(val key: String?)
}