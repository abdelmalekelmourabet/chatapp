package com.hva.chatapp

import com.xwray.groupie.*
import kotlinx.android.synthetic.main.chat_right.view.*

class ChatRightItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvRight.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_right
    }
}