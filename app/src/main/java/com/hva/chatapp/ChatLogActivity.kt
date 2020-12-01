package com.hva.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_left.view.*
import kotlinx.android.synthetic.main.chat_right.view.*

class ChatLogActivity : AppCompatActivity() {
    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        rvChatLog.adapter = adapter

        //Print username in Chat Log
        toUser = intent.getParcelableExtra<User>(NewChatActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()

        btnSend.setOnClickListener {
            Log.d("chatlog", "attempt to send message")
            performSendMessage()
        }
    }


    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("chatlog", chatMessage.text)
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

        val reference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference =
            FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val chatMessage =
            ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis())

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d("chatlog", "saved our message ${reference.key}")
                etChatLog.text.clear()
                rvChatLog.scrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)

        val homeRef = FirebaseDatabase.getInstance().getReference("/home-messages/$fromId/$toId")
        val homeToRef = FirebaseDatabase.getInstance().getReference("/home-messages/$toId/$fromId")
        homeRef.setValue(chatMessage)
        homeToRef.setValue(chatMessage)
    }
}