package com.hva.chatapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.hva.chatapp.R
import com.hva.chatapp.model.ChatLeftItem
import com.hva.chatapp.model.Message
import com.hva.chatapp.model.ChatRightItem
import com.hva.chatapp.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*


class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        rvChatLog.adapter = adapter

        //Print username in Chat Log
        toUser = intent.getParcelableExtra(NewChatActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()

        btnSend.setOnClickListener {
            performSendMessage()
        }
    }


    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {

            //add new messages in chat
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(Message::class.java)
                if (chatMessage != null) {
                    if (chatMessage.toId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatLeftItem(chatMessage.text, toUser!!))
                    } else {
                        adapter.add(ChatRightItem(chatMessage.text, toUser!!))
                    }
                }
                rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }

            // necessarily but not needed
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}

        })
    }

    // post message in firebase
    private fun performSendMessage() {
        val text = etChatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewChatActivity.USER_KEY)
        val toId = user?.uid

        if (fromId == null || toId == null) return

        // Make the message visible on both screens
        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage =
            Message(reference.key!!, text, fromId, toId, System.currentTimeMillis())

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                etChatLog.text.clear()
                rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        // Make the message visible on homescreen on both screens
        val homeRef = FirebaseDatabase.getInstance().getReference("/home-messages/$fromId/$toId")
        val homeToRef = FirebaseDatabase.getInstance().getReference("/home-messages/$toId/$fromId")
        homeRef.setValue(chatMessage)
        homeToRef.setValue(chatMessage)
    }
}