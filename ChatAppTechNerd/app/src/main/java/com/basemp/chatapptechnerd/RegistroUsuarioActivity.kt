package com.basemp.chatapptechnerd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.basemp.chatapptechnerd.entities.UserEntity
import com.basemp.chatapptechnerd.model.FirestoreModel
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref
import com.basemp.chatapptechnerd.storage.IMStorageInjection
import com.basemp.chatapptechnerd.storage.MDataInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registro_usuario.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class RegistroUsuarioActivity : AppCompatActivity(), CoroutineScope {
    private var mAuth: FirebaseAuth? = null
    private var firestoreModel: FirestoreModel? = null
    lateinit var mStorageInjection: IMStorageInjection
    private var sp: MDefaultSharedPref? = null

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)
        mStorageInjection=  MDataInjection.instance()
        mStorageInjection.setUp(this)
        sp = MDataInjection.instance().sharedPreferenceManager() as MDefaultSharedPref
        firestoreModel = FirestoreModel()
        if(sp!!.isSession()) {
            val intent = Intent(this,ContactsActivity::class.java)
            startActivity(intent)
        }
        mAuth = FirebaseAuth.getInstance()
        firestoreModel = FirestoreModel()
        setupEvents()

    }

    private fun setupEvents() {
        btnRegistrar.setOnClickListener {
            var firtName = txtUserNameReg.text.toString()
            var lastName = txtUserLastnameReg.text.toString()
            var email = txtUserEmailReg.text.toString()
            var phoneNumber = txtUserPhoneReg.text.toString()
            var pass = txtPasswordReg.text.toString()
            var passconfirm = txtPasswordConfirmReg.text.toString()

            if(firtName == "" || lastName == ""){
                Toast.makeText(this, "Names are required.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(email == ""){
                Toast.makeText(this, "Email is required.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(pass == "" || passconfirm == ""){
                Toast.makeText(this, "Passwords are required.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!pass.equals(passconfirm)){
                Toast.makeText(this, "Passwords must match",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mAuth!!.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(
                    this
                ) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Success registration.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val currentUser = mAuth!!.currentUser
                        var entityUser = UserEntity(firstName =  firtName, lastName =  lastName,
                            email =  currentUser!!.email, contactNumber = phoneNumber)
                        lifecycleScope.launch{
                            var iduser = firestoreModel!!.saveUser(entityUser)
                            goToHome()
                        }

                    } else {
                        Toast.makeText(
                            this, "You must enter a valid email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    fun goToHome(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}