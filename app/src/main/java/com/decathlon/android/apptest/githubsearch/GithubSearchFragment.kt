package com.decathlon.android.apptest.githubsearch


import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.decathlon.android.apptest.R
import com.decathlon.android.apptest.common.usecase.SearchGitHubRepositories
import com.decathlon.android.apptest.data.entity.search.Repository
import com.decathlon.android.apptest.data.repository.search.RemoteSearchRepository
import com.jakewharton.rxbinding3.widget.editorActionEvents
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_github_search.*
import java.util.concurrent.TimeUnit


class GithubSearchFragment() : Fragment(), GithubSearchContract.View {
    override lateinit var presenter: GithubSearchContract.Presenter
    private lateinit var textViewObserver: Disposable
    private lateinit var githubSearchAdapter: GithubSearchAdapter
    private lateinit var choosenOs: GithubSearchContract.Companion.System
    private var firstLaunch = true

    companion object {
        fun newInstance(): GithubSearchFragment {
            return GithubSearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_github_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        radiogroup_search.setOnCheckedChangeListener { group, checkedId ->
            choosenOs = when (checkedId) {
                R.id.radiobutton_search_android -> GithubSearchContract.Companion.System.ANDROID
                R.id.radiobutton_search_ios -> GithubSearchContract.Companion.System.IOS
                else -> GithubSearchContract.Companion.System.ANDROID
            }
            presenter.searchRepositoriesOnGitHub(edittext_search.text.toString(), choosenOs)
        }

        recyclerview_search.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = githubSearchAdapter
            addItemDecoration(DividerItemDecoration(this@GithubSearchFragment.context, DividerItemDecoration.VERTICAL))
        }

        edittext_search
            .setOnEditorActionListener { editText, actionId, event ->
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    presenter.searchRepositoriesOnGitHub(editText.text.toString(), choosenOs)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        textViewObserver = edittext_search
            .textChanges()
            .debounce(1, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe() {
                if (firstLaunch) {
                    firstLaunch = false
                } else {
                    presenter.searchRepositoriesOnGitHub(it.toString(), choosenOs)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        textViewObserver.dispose()
        presenter.onDestroy()
    }

    override fun showLoadingView() {
        activity?.runOnUiThread {
            loading_search.visibility = View.VISIBLE
            recyclerview_search.visibility = View.INVISIBLE
            textview_search_emptyresult.visibility = View.GONE
            edittext_search.isEnabled = false
        }
    }

    override fun hideLoadingView() {
        activity?.runOnUiThread {
            loading_search.visibility = View.GONE
            recyclerview_search.visibility = View.VISIBLE
            edittext_search.isEnabled = true
        }
    }

    override fun showEmptyViewMessage() {
        activity?.runOnUiThread {
            textview_search_emptyresult.visibility = View.VISIBLE
            recyclerview_search.visibility = View.INVISIBLE
        }
    }

    override fun showEmptyInputError() {
        activity?.runOnUiThread {
            textinputlayout_search.error = getString(R.string.common_emptyinput_error)
        }
    }

    override fun clearInputError() {
        activity?.runOnUiThread {
            textinputlayout_search.isErrorEnabled = false
        }
    }

    override fun showConnectivityIssueMessage() {
        showAlertView(getString(R.string.common_connectivityissue))
    }

    override fun showTimeoutIssueMessage() {
        showAlertView(getString(R.string.common_timeoutissue))
    }

    override fun showApiRateLimitMessage() {
        showAlertView(getString(R.string.common_apirateissue))
    }

    override fun showUnknownErrorMessage() {
        showAlertView(getString(R.string.common_unknownissue))
    }

    override fun closeKeyboard() {
        activity?.runOnUiThread {
            val inputManager = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

    }

    override fun displaySearchResult(list: List<Repository>) {
        if (list.isEmpty()) {
            showEmptyViewMessage()
        } else {
            this.githubSearchAdapter.references = list
        }
    }

    private fun init() {
        val searchRepository = RemoteSearchRepository(context)
        val searchGitHubRepositories = SearchGitHubRepositories(searchRepository)
        choosenOs = GithubSearchContract.Companion.System.ANDROID

        presenter = GithubSearchPresenter(
            this,
            searchGitHubRepositories
        )
        githubSearchAdapter = GithubSearchAdapter()
    }

    private fun showAlertView(message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.common_error))
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> }
        builder.show()
    }
}
