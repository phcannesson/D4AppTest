package com.decathlon.android.apptest.githubsearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decathlon.android.apptest.R
import com.decathlon.android.apptest.data.entity.search.Repository
import kotlinx.android.synthetic.main.item_search_repository.view.*

class GithubSearchAdapter : RecyclerView.Adapter<GithubSearchAdapter.RepositoryViewHolder>() {

    var references: List<Repository>? = emptyList()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_repository, parent, false)
        return RepositoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return references?.size as Int
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(references?.get(position))
    }

    class RepositoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reference: Repository?) = with(itemView) {
            textview_itemsearch_title.text = reference?.fullName
            textview_itemsearch_desc.text = reference?.description
            textview_itemsearch_stars.text = reference?.stargazersCount.toString()

            Glide
                .with(itemView.context)
                .load(reference?.owner?.avatarUrl)
                .into(imageview_itemsearch_author)
        }
    }
}