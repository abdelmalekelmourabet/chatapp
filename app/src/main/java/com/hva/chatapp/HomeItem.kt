package com.hva.chatapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.home_item.view.*


class HomeItem(val chatMessage: ChatMessage) : Item<GroupieViewHolder>() {
    var chatFriendUser: User? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvLatestMessage.text = chatMessage.text

        // Find username in database
        val chatFriendId: String
        if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            chatFriendId = chatMessage.toId
        } else {
            chatFriendId = chatMessage.fromId
        }

        // Get Username
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatFriendId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chatFriendUser  = snapshot.getValue(User::class.java)
                viewHolder.itemView.tvUsername.text = chatFriendUser?.username
            }

            override fun onCancelled(p0: DatabaseError) {}
        })


    }

    override fun getLayout(): Int {
        return R.layout.home_item
    }
}