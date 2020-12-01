package com.hva.chatapp.model

import com.hva.chatapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_item.view.*

class UserItem(val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvUsernameNewChat.text = user.username

    }

    override fun getLayout(): Int {
        return R.layout.user_item
    }
}