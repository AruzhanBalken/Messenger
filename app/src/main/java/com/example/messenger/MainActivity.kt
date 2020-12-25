package com.example.messenger

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_register.setOnClickListener(){
        Register()

        }

        already_have_acc_register.setOnClickListener(){
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

    button2.setOnClickListener{
        val intent = Intent(this, EditUserActivity::class.java)
        startActivity(intent)
    }
    }
    private fun Register(){
        val email = email_register.text.toString()
        val password = password_register.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please fill in email and password fields", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("MainActivity", "email:  $email")
        Log.d("MainActivity", "psswd: $password" )
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Main", "success: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed to create new user: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("Main", "Failed to create new user: ${it.message}")
            }
        saveUser()
    }

    private fun saveUser(){
        val uid = FirebaseAuth.getInstance().uid?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username_register.text.toString(), email_register.text.toString(), password_register.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("register", "saved user to db")
            val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

    }
}
