package com.decathlon.android.apptest.githubsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.decathlon.android.apptest.R

class GithubSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.framelayout_githubsearch_fragmentcontainer, GithubSearchFragment.newInstance())
            .commit()
    }
}
