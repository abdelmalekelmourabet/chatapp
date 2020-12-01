package com.hva.chatapp.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hva.chatapp.ui.HomeActivity
import com.hva.chatapp.R
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_signin)

        btnLogin.setOnClickListener {
            performLogin()
        }

        tvBackToRegister.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }


    private fun performLogin() {
        val email = etEmailLogin.text.toString()
        val password = etPasswordLogin.text.toString()
        val fieldNotEmpty = getString(R.string.field_not_empty)
        val failedCreateUser = getString(R.string.failed_create_user)

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, fieldNotEmpty, Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Toast.makeText(this, failedCreateUser + " ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}