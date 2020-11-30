package com.hva.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_left.view.*
import kotlinx.android.synthetic.main.chat_right.view.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //Print username in Chat Log
        //val username= intent.getStringExtra(NewChatActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewChatActivity.USER_KEY)
        supportActionBar?.title = user?.username



        btnSend.setOnClickListener {
            Log.d("chatlog", "attempt to send message")
            performSendMessage()
        }
    }

    // post message in firebase
    private fun performSendMessage() {
        val text = etChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewChatActivity.USER_KEY)
        val toId = user?.uid

        if (fromId == null || toId == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val chatMessage =
            ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis())

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("chatlog", "saved our message ${reference.key}")
            }
    }
}


class ChatRightItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvRight.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_right
    }
}

class ChatLeftItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvLeft.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_left
    }
}

class ChatMessage(
    val id: String,
    val text: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "", -1)
}