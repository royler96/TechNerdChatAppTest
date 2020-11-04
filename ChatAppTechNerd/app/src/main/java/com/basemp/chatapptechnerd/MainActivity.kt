package com.basemp.chatapptechnerd

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        iniciarSesion()
        registrarUsuario()
    }
    override fun onStart() {
        super.onStart()
    }

    private fun iniciarSesion() {
        btnIngresar.setOnClickListener{
            mAuth!!.signInWithEmailAndPassword(txtUsuario.text.toString(), txtPassword.text.toString())
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this,ContactsActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this, "Wrong credentials.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun registrarUsuario(){
        btnRegistrarse.setOnClickListener {
            val intent = Intent(this,RegistroUsuarioActivity::class.java)
            startActivity(intent)
        }

    }
}