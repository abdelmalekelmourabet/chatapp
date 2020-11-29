package com.hva.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btnRegister.setOnClickListener {
            performRegister()
        }

        // launch the login activity
        tvAlreadyAnAccount.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }
    }

    // Register user in Firebase
    private fun performRegister() {
        val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()
        val fieldNotEmpty = getString(R.string.field_not_empty)
        val failedCreateUser = getString(R.string.failed_create_user)

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, fieldNotEmpty, Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                it.result?.user?.let { it1 -> saveUserToFirebase(it1.uid) }
                Log.d("signup", "save user in db")

            }

            .addOnFailureListener {
                Toast.makeText(this, failedCreateUser + " ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun saveUserToFirebase(uid: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, etUsernameRegister.text.toString(), etEmailRegister.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {

                val intent = Intent(this, ChatActivity::class.java)

                //Prevent going back to Signup page
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)


            }
            .addOnFailureListener {
                Log.d("signup", "Failed to set value to database: ${it.message}")
            }
    }
}

