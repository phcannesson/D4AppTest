package com.decathlon.android.apptest.data.entity.search

import com.squareup.moshi.Json

data class SearchResult(

    @Json(name = "total_count")
    val totalCount: Int? = null,

    @Json(name = "incomplete_results")
    val incompleteResults: Boolean? = null,

    @Json(name = "items")
    val items: List<Repository?>? = null
)