package com.decathlon.android.apptest.data.repository.search

import com.decathlon.android.apptest.data.entity.search.SearchResult
import io.reactivex.Single

interface SearchRepository {
    fun searchOnGithub(query: String): Single<SearchResult>
}