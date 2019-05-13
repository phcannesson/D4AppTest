package com.decathlon.android.apptest.data.repository.search

import com.decathlon.android.apptest.data.entity.search.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SearchService {
    @GET("/search/repositories")
    fun searchRepositories(
        @Query(
            "q",
            encoded = true
        ) query: String, @QueryMap additionalParams: Map<String, String>
    ): Call<SearchResult>
}