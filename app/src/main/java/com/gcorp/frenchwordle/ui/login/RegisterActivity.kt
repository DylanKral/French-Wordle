package com.gcorp.frenchwordle.ui.login

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

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
    }

    fun onClickRegister(v: View?) {
        val editTextEmail = findViewById<EditText>(R.id.editTextTextEmailAddress).text
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPassword).text

        val email = editTextEmail.toString()
        val password = editTextPassword.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("REGISTER", "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        baseContext,
                        "You successfully created an account into the awesome app!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("REGISTER", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "SignIn up failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
