package com.gcorp.frenchwordle.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.gcorp.frenchwordle.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
    }

    fun onClickLogin(v: View?) {
        val editTextEmail = findViewById<EditText>(R.id.editTextTextEmailAddress).text
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPassword).text

        val email = editTextEmail.toString()
        val password = editTextPassword.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "LogginWithEmail:success")
                    val user = auth.currentUser
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "LogginWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Either your id or password is incorrect",
                        Toast.LENGTH_SHORT).show()
                }
            }

        //Log.d("DEBUG", editTextPassword.toString())
        //Log.d("DEBUG", editTextPasswordConfirmation.toString())
    }

    fun onClickRegister(v: View?) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}