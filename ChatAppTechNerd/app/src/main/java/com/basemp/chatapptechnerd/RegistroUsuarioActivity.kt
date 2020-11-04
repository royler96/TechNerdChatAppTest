package com.basemp.chatapptechnerd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro_usuario.*

class RegistroUsuarioActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        mAuth = FirebaseAuth.getInstance()
        setupEvents()
    }

    private fun setupEvents() {
        btnRegistrar.setOnClickListener {
            mAuth!!.createUserWithEmailAndPassword(txtUsuarioReg.text.toString(), txtPasswordReg.text.toString())
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Success registration.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this,ContactsActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this, "You must enter a valid email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}