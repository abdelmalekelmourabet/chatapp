package com.hva.chatapp

import com.xwray.groupie.*
import kotlinx.android.synthetic.main.chat_left.view.*

class ChatLeftItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvLeft.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_left
    }
}
