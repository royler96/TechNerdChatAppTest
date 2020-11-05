package com.basemp.chatapptechnerd

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.basemp.chatapptechnerd.entities.UserEntity
import com.basemp.chatapptechnerd.model.FirestoreModel
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref
import com.basemp.chatapptechnerd.storage.IMStorageInjection
import com.basemp.chatapptechnerd.storage.MDataInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private var mAuth: FirebaseAuth? = null
    lateinit var mStorageInjection: IMStorageInjection
    private var sp: MDefaultSharedPref? = null
    private var firestoreModel: FirestoreModel? = null

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mStorageInjection=  MDataInjection.instance()
        mStorageInjection.setUp(this)
        sp = MDataInjection.instance().sharedPreferenceManager() as MDefaultSharedPref
        if(sp!!.isSession()){
            val intent = Intent(this,ContactsActivity::class.java)
            startActivity(intent)
        }

        firestoreModel = FirestoreModel()
        mAuth = FirebaseAuth.getInstance()
        iniciarSesion()
        registrarUsuario()
        forgotPassword()
    }

    private fun iniciarSesion() {
        btnIngresar.setOnClickListener{
            mAuth!!.signInWithEmailAndPassword(txtUsuario.text.toString(), txtPassword.text.toString())
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        val currentUser = mAuth!!.currentUser
                        lifecycleScope.launch{
                        var entityUser =
                            firestoreModel!!.getUserEmail(email = currentUser!!.email.toString())
                        entityUser?.let {
                            if (it.id != "") {
                                sp!!.saveSession(entityUser!!)
                                PantallaInicio()
                            }
                        }
                    }

                    } else {
                        Toast.makeText(
                            this, "Wrong credentials.",
                            Toast.LENGTH_LONG
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
    private fun forgotPassword() {
        btnForgotPass.setOnClickListener{
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
    private fun PantallaInicio(){
        val intent = Intent(this,ContactsActivity::class.java)
        startActivity(intent)
    }
}