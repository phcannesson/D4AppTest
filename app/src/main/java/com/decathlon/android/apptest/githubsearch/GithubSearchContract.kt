package com.decathlon.android.apptest.githubsearch

import com.decathlon.android.apptest.common.base.BasePresenter
import com.decathlon.android.apptest.common.base.BaseView
import com.decathlon.android.apptest.data.entity.search.Repository

interface GithubSearchContract {
    interface View : BaseView<Presenter> {

        fun showLoadingView()

        fun hideLoadingView()

        fun displaySearchResult(list: List<Repository>)

        fun showEmptyViewMessage()

        fun showEmptyInputError()

        fun clearInputError()

        fun showConnectivityIssueMessage()

        fun showTimeoutIssueMessage()

        fun showApiRateLimitMessage()

        fun showUnknownErrorMessage()

        fun closeKeyboard()
    }

    interface Presenter : BasePresenter {
        fun searchRepositoriesOnGitHub(query: String, system: System)
    }

    companion object {
        enum class System { ANDROID, IOS }
    }
}