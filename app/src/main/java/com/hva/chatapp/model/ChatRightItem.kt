package com.hva.chatapp.model

import com.hva.chatapp.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_right.view.*

class ChatRightItem(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvRight.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_right
    }
}