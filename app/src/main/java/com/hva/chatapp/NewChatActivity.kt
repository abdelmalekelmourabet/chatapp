package com.hva.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class NewChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        val selectUser = getString(R.string.select_user)

        supportActionBar?.title = selectUser
    }
}