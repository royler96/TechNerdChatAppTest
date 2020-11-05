package com.basemp.chatapptechnerd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basemp.chatapptechnerd.entities.ContactEntity
import com.basemp.chatapptechnerd.model.FirestoreModel
import com.basemp.chatapptechnerd.preferences.MDefaultSharedPref
import com.basemp.chatapptechnerd.storage.IMStorageInjection
import com.basemp.chatapptechnerd.storage.MDataInjection
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.find
import kotlin.coroutines.CoroutineContext

class ContactsActivity : AppCompatActivity(), CoroutineScope {
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
        setContentView(R.layout.activity_contacts)

        mStorageInjection=  MDataInjection.instance()
        mStorageInjection.setUp(this)
        sp = MDataInjection.instance().sharedPreferenceManager() as MDefaultSharedPref
        firestoreModel = FirestoreModel()

        var dataUser = sp!!.session()
        lifecycleScope.launch{
            var arrayListContacts = firestoreModel!!.getContactsByUser(dataUser!!.id!!)
            readRVContacts(arrayListContacts)
        }
        searchContact()
        addContact()
        mAuth = FirebaseAuth.getInstance()
        btnCloseContact.setOnClickListener{
            mAuth!!.signOut()
            sp!!.clearSession()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun readRVContacts(arrayListContacts: List<ContactEntity>) {
        var myRvListContacts: RecyclerView = find(R.id.rvListContacts)
        myRvListContacts.layoutManager = LinearLayoutManager(this)
        myRvListContacts.adapter = ContactsAdapter(arrayListContacts)
    }

    fun searchContact(){
        btnSearchContact.setOnClickListener{
            var textSearch = etSearchContact.text.toString()
            var dataUser = sp!!.session()
            lifecycleScope.launch{
                if(textSearch == ""){
                    var arrayListContacts = firestoreModel!!.getContactsByUser(dataUser!!.id!!)
                    readRVContacts(arrayListContacts)
                }else{
                    var arrayListContacts = firestoreModel!!.getContactsByUserTextSearch(dataUser!!.id!!, textSearch)
                    readRVContacts(arrayListContacts)
                }
            }
        }
    }

    fun addContact() {
        btnAddContact.setOnClickListener{
            val intent = Intent(this,ContactRegisterActivity::class.java)
            startActivity(intent)
        }
    }
}