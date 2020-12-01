package com.hva.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        rvHome.adapter = adapter
        rvHome.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)
            val row = item as HomeItem
            intent.putExtra(NewChatActivity.USER_KEY, row.chatFriendUser)
            startActivity(intent)
        }

        checkIfUserAlreadyLoggedin()
        listenForHomeMessages()
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
    val homeMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewHome() {
        adapter.clear()
        homeMap.values.forEach {
            adapter.add(HomeItem(it))
        }
    }

    private fun listenForHomeMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/home-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                homeMap[p0.key!!] = chatMessage
                refreshRecyclerViewHome()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                homeMap[p0.key!!] = chatMessage
                refreshRecyclerViewHome()
            }

            // necessarily but not needed
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
        })
    }

    private fun checkIfUserAlreadyLoggedin() {
        val uid = FirebaseAuth.getInstance().uid

        if (uid == null) {
            val intent = Intent(this, SignupActivity::class.java)

            //Prevent going back to Signup page
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    //Start new activity when clicked on menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewChatActivity::class.java)
                startActivity(intent)
            }

            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, SignupActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Launch menu onCreate
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}