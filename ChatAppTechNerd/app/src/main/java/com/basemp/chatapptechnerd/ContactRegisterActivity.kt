package com.basemp.chatapptechnerd

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.lifecycleScope
import com.basemp.chatapptechnerd.entities.ContactEntity
import com.basemp.chatapptechnerd.model.FirestoreModel
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref
import com.basemp.chatapptechnerd.storage.IMStorageInjection
import com.basemp.chatapptechnerd.storage.MDataInjection
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_contact_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class ContactRegisterActivity : AppCompatActivity(), CoroutineScope {
    lateinit var mStorageInjection: IMStorageInjection
    private var sp: MDefaultSharedPref? = null
    private var firestoreModel: FirestoreModel? = null

    private var idContact = ""

    private var job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_register)

        mStorageInjection=  MDataInjection.instance()
        mStorageInjection.setUp(this)
        sp = MDataInjection.instance().sharedPreferenceManager() as MDefaultSharedPref
        firestoreModel = FirestoreModel()

        if(intent.getStringExtra("id") != null){
            idContact = intent.getStringExtra("id")!!

            var et1 = txtContactNameReg as TextInputEditText
            et1.setText(intent.getStringExtra("firstname")!!.toString())

            var et2 = txtContactLastnameReg as TextInputEditText
            et2.setText(intent.getStringExtra("lastname")!!.toString())

            var et3 = txtContactEmailReg as TextInputEditText
            et3.setText(intent.getStringExtra("email")!!.toString())

            var et4 = txtContactPhoneReg as TextInputEditText
            et4.setText(intent.getStringExtra("phone")!!.toString())

            var btnDelete = btnContactDelete as Button
            btnDelete.isInvisible = false
            updateContact()
            deleteContact()
        } else{
            var btnDelete = btnContactDelete as Button
            btnDelete.isInvisible = true
            registerContact()
        }
    }

    fun registerContact(){
        btnContactRegister.setOnClickListener{
            var firtName = txtContactNameReg.text.toString()
            var lastName = txtContactLastnameReg.text.toString()
            var email = txtContactEmailReg.text.toString()
            var phoneNumber = txtContactPhoneReg.text.toString()

            if(firtName == "" || lastName == ""){
                Toast.makeText(this, "Names are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(email == ""){
                Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var dataUser = sp!!.session()
            lifecycleScope.launch{
                var contactData = ContactEntity(firstname = firtName, lastname = lastName, email = email, phone = phoneNumber)
                var response = firestoreModel!!.saveContact(dataUser!!.id!!, contactData)
                registerContactMessage(response)
            }
        }
    }
    fun updateContact() {
        btnContactRegister.setOnClickListener{
            var firtName = txtContactNameReg.text.toString()
            var lastName = txtContactLastnameReg.text.toString()
            var email = txtContactEmailReg.text.toString()
            var phoneNumber = txtContactPhoneReg.text.toString()

            if(firtName == "" || lastName == ""){
                Toast.makeText(this, "Names are required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(email == ""){
                Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var dataUser = sp!!.session()
            lifecycleScope.launch{
                var contactData = ContactEntity(id = idContact, firstname = firtName, lastname = lastName, email = email, phone = phoneNumber)
                var response = firestoreModel!!.updateContact(dataUser!!.id!!, contactData)
                updateContactMessage(response)
            }
        }
    }

    fun deleteContact() {
        btnContactDelete.setOnClickListener{
            var dataUser = sp!!.session()
            lifecycleScope.launch{
                var contactData = ContactEntity(id = idContact)
                var response = firestoreModel!!.deleteContact(dataUser!!.id!!, contactData)
                deleteContactMessage(response)
            }
        }
    }

    private fun registerContactMessage(response: String){
        if(response != ""){
            Toast.makeText(this, "Successful registration.", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }, 600)
        } else{
            Toast.makeText(this, "Failed to register.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateContactMessage(response: String){
        if(response != ""){
            Toast.makeText(this, "Successful registration.", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(this, ContactsActivity::class.java)
                startActivity(intent)
            }, 600)
        } else{
            Toast.makeText(this, "Failed to register.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteContactMessage(response: String){
        if(response != ""){
            Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({
                val intent = Intent(this, ContactsActivity::class.java)
                startActivity(intent)
            }, 600)
        } else{
            Toast.makeText(this, "Failed to delete.", Toast.LENGTH_SHORT).show()
        }
    }
}