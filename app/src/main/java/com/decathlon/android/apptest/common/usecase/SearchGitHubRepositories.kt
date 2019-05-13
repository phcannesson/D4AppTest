package com.decathlon.android.apptest.common.usecase

import com.decathlon.android.apptest.common.base.SingleUseCase
import com.decathlon.android.apptest.data.entity.search.SearchResult
import com.decathlon.android.apptest.data.repository.search.SearchRepository
import io.reactivex.Single

class SearchGitHubRepositories(private val searchRepository: SearchRepository) : SingleUseCase<SearchResult, String>() {
    override fun buildUseCaseObservable(params: String?): Single<SearchResult> {
        return params?.let { searchRepository.searchOnGithub(it) } as Single<SearchResult>
    }

}