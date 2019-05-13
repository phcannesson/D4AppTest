package com.decathlon.android.apptest.data.repository.search

import android.content.Context
import com.decathlon.android.apptest.common.exception.EmptyBodyException
import com.decathlon.android.apptest.common.exception.ForbiddenException
import com.decathlon.android.apptest.common.exception.UnknownErrorException
import com.decathlon.android.apptest.common.network.NetworkConstants
import com.decathlon.android.apptest.data.ServiceGenerator
import com.decathlon.android.apptest.data.entity.search.SearchResult
import io.reactivex.Single
import java.security.InvalidParameterException
import java.util.*

class RemoteSearchRepository(private val context: Context?) : SearchRepository {
    override fun searchOnGithub(query: String): Single<SearchResult> {
        return Single.create {

            val additionalParams = HashMap<String, String>()
            additionalParams[NetworkConstants.PARAM_PAGE_NUMBER_KEY] = NetworkConstants.PARAM_PAGE_NUMBER_VALUE
            additionalParams[NetworkConstants.PARAM_PAGE_RESULT_KEY] = NetworkConstants.PARAM_PAGE_RESULT_VALUE

            if (context == null) {
                return@create it.onError(InvalidParameterException())
            }

            val searchService = ServiceGenerator.createService(SearchService::class.java, context)
            val searchRepositories = searchService.searchRepositories(query, additionalParams)

            try {
                val response = searchRepositories.execute()
                when (response.code()) {
                    in 200..300 -> {
                        val searchResult = response.body()
                        if (searchResult != null) {
                            return@create it.onSuccess(searchResult)
                        } else {
                            return@create it.onError(EmptyBodyException())
                        }
                    }
                    403 -> {
                        return@create it.onError(ForbiddenException())
                    }
                }
                it.onError(UnknownErrorException())
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }

}