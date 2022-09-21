package com.app.kmmtumbler.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.kmmtumbler.data.UserFollowing

class UserFollowingDataAdapter :
    PagingDataAdapter<UserFollowing, UserFollowingDataAdapter.UserFollowingDataViewHolder>(userFollowingDiffUtil) {


    class UserFollowingDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val userFollowingData: TextView = itemView.findViewById(R.id.userFollowingData)

        fun bind(data: UserFollowing) {
            userFollowingData.text = data.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserFollowingDataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user_following, parent, false)
        return UserFollowingDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserFollowingDataViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

val userFollowingDiffUtil = object : DiffUtil.ItemCallback<UserFollowing>() {

    override fun areItemsTheSame(oldItem: UserFollowing, newItem: UserFollowing): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: UserFollowing, newItem: UserFollowing): Boolean {
        return oldItem.uuid == newItem.uuid
    }
}
