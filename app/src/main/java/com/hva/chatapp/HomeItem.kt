package com.hva.chatapp

import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_item.view.*
import kotlinx.android.synthetic.main.user_item.view.*

class HomeItem(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvLatestMessage.text = chatMessage.text
    }

    override fun getLayout(): Int {
        return R.layout.chat_item
    }
}