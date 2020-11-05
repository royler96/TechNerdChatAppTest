package com.basemp.chatapptechnerd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.basemp.chatapptechnerd.model.FirestoreModel
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref
import com.basemp.chatapptechnerd.storage.IMStorageInjection
import com.basemp.chatapptechnerd.storage.MDataInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    lateinit var mStorageInjection: IMStorageInjection
    private var sp: MDefaultSharedPref? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mStorageInjection=  MDataInjection.instance()
        mStorageInjection.setUp(this)
        sp = MDataInjection.instance().sharedPreferenceManager() as MDefaultSharedPref
        if(sp!!.isSession()) {
            val intent = Intent(this,ContactsActivity::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()
        resetPassword()
    }
    fun resetPassword(){
        btnEmailForgot.setOnClickListener{
            mAuth!!.sendPasswordResetEmail(txtEmailForgot.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "Send email.",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this, "Error when sending the email to reset the password.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}