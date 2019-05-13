package com.decathlon.android.apptest.githubsearch

import com.decathlon.android.apptest.common.exception.EmptyBodyException
import com.decathlon.android.apptest.common.exception.ForbiddenException
import com.decathlon.android.apptest.common.exception.NoConnectivityException
import com.decathlon.android.apptest.common.usecase.SearchGitHubRepositories
import com.decathlon.android.apptest.data.entity.search.Repository
import com.decathlon.android.apptest.data.entity.search.SearchResult
import io.reactivex.observers.DisposableSingleObserver
import java.net.SocketTimeoutException

class GithubSearchPresenter(
    private val gitHubSearchView: GithubSearchContract.View,
    private val searchGitHubRepositories: SearchGitHubRepositories
) : GithubSearchContract.Presenter {

    var isASearchInProgress : Boolean = false

    init {
        gitHubSearchView.presenter = this
    }

    override fun searchRepositoriesOnGitHub(query: String, system: GithubSearchContract.Companion.System) {
        if (query.isNotEmpty()) {
            if(!isASearchInProgress) {
                gitHubSearchView.showLoadingView()
                gitHubSearchView.clearInputError()
                gitHubSearchView.closeKeyboard()
                val queryComplement = when (system) {
                    GithubSearchContract.Companion.System.ANDROID -> ANDROID_APPEND_QUERY
                    GithubSearchContract.Companion.System.IOS -> IOS_APPEND_QUERY
                }
                searchGitHubRepositories.execute(SearchGitHubRepositoriesObserver(), "$query$queryComplement")
                isASearchInProgress = true
            }
        } else {
            gitHubSearchView.showEmptyInputError()
        }
    }

    override fun onDestroy() {
        this.searchGitHubRepositories.dispose()
    }

    private inner class SearchGitHubRepositoriesObserver : DisposableSingleObserver<SearchResult>() {
        override fun onSuccess(results: SearchResult) {
            gitHubSearchView.hideLoadingView()
            isASearchInProgress = false
            results.items?.filterIsInstance<Repository>()?.let { gitHubSearchView.displaySearchResult(it) }
        }

        override fun onError(e: Throwable) {
            gitHubSearchView.hideLoadingView()
            isASearchInProgress = false
            when (e) {
                is NoConnectivityException -> gitHubSearchView.showConnectivityIssueMessage()
                is SocketTimeoutException -> gitHubSearchView.showTimeoutIssueMessage()
                is EmptyBodyException -> gitHubSearchView.showEmptyViewMessage()
                is ForbiddenException -> gitHubSearchView.showApiRateLimitMessage()
                else -> gitHubSearchView.showUnknownErrorMessage()
            }
        }

    }

    companion object {
        private const val ANDROID_APPEND_QUERY = "+in:nametopic:android+language:java+language:kotlin"
        private const val IOS_APPEND_QUERY = "+in:nametopic:ios+language:objectivec+language:swift"
    }
}