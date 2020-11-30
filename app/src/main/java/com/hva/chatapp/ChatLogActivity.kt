package com.hva.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hva.chatapp.NewChatActivity.Companion.USER_KEY
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //Print username in Chat Log
        //val username= intent.getStringExtra(NewChatActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewChatActivity.USER_KEY)
        supportActionBar?.title = user?.username

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatRightItem())
        adapter.add(ChatLeftItem())
        adapter.add(ChatRightItem())
        adapter.add(ChatLeftItem())
        adapter.add(ChatRightItem())
        adapter.add(ChatLeftItem())
        adapter.add(ChatRightItem())
        adapter.add(ChatLeftItem())

        rvChatLog.adapter = adapter
    }
}

class ChatRightItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_right
    }
}

class ChatLeftItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_left
    }
}