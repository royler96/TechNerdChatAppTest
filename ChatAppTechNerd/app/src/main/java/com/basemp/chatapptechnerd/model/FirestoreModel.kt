package com.basemp.chatapptechnerd.model

import com.basemp.chatapptechnerd.entities.ContactEntity
import com.basemp.chatapptechnerd.entities.UserEntity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreModel : IFirestoreModel {
    val db = Firebase.firestore
    override fun getUser(id: String): UserEntity? {
        var datos: UserEntity? = null
        db.collection("users")
            .document(id).get()
            .addOnSuccessListener { document ->
                datos = document.toObject<UserEntity>()
            }
            .addOnFailureListener { exception ->
                datos = null
            }
        return datos
    }
    override suspend fun getUserEmail(email: String): UserEntity? {
        var resultado = UserEntity()
        var emailLower = email!!.toLowerCase()
         var docs = db.collection("users")
            .whereEqualTo("email",emailLower).get().await()
        for (document in docs){
            resultado.firstName = document.data["firstName"].toString()
            resultado.lastName = document.data["lastName"].toString()
            resultado.email = document.data["email"].toString()
            resultado.contactNumber = document.data["contactNumber"].toString()
            resultado.id = document.id
        }
        return resultado
    }
    override suspend fun saveUser(user: UserEntity) : String {
        var idUser = ""
        var emailLower = user.email!!.toLowerCase()
        var documents = db.collection("users").whereEqualTo("email",emailLower).get().await()
        for (doc in documents){
            if(doc.get("email").toString() == emailLower){
                idUser = doc.id
            }
        }
        if(idUser == "") {
            val userMap = hashMapOf(
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "email" to emailLower,
                "contactNumber" to user.contactNumber
            )
           var result = db.collection("users")
                .add(userMap).await()
            idUser = result.id
        }
        return idUser
    }

    override suspend fun getContactsByUser(coduser: String): List<ContactEntity> {
        var codigo = "/users/${coduser}"
        var docs = db.collection("users").document(coduser)
                        .collection("contactsperson").get().await()//.whereEqualTo("iduser", "users/${coduser}").get().await()

        var datos = ArrayList<ContactEntity>()
        for (document in docs){
            var data = ContactEntity()
            data.firstname = document.data["firstname"].toString()
            data.lastname = document.data["lastname"].toString()
            data.email = document.data["email"].toString()
            data.phone = document.data["phone"].toString()
            data.id = document.id
            datos.add(data)
        }
        return datos
    }
    override suspend fun getContactsByUserTextSearch(coduser: String, textSearch: String): List<ContactEntity> {
        var codigo = "/users/${coduser}"
        var docs = db.collection("users").document(coduser)
            .collection("contactsperson").whereEqualTo("firstname", textSearch).get().await()//.whereEqualTo("iduser", "users/${coduser}").get().await()

        var datos = ArrayList<ContactEntity>()
        for (document in docs){
            var data = ContactEntity()
            data.firstname = document.data["firstname"].toString()
            data.lastname = document.data["lastname"].toString()
            data.email = document.data["email"].toString()
            data.phone = document.data["phone"].toString()
            data.id = document.id
            datos.add(data)
        }
        return datos
    }

    override suspend fun saveContact(iduser: String, contact: ContactEntity) : String {
        var dataContact = hashMapOf(
            "firstname" to contact.firstname,
            "lastname" to contact.lastname,
            "email" to contact.email,
            "phone" to contact.phone
        )

        var resultSave = db.collection("users").document(iduser).collection("contactsperson")
            .add(dataContact).await()

        return resultSave.id
    }
    override suspend fun updateContact(iduser: String, contact: ContactEntity) : String {
        db.collection("users").document(iduser).collection("contactsperson")
            .document(contact!!.id!!).update("firstname", contact.firstname,
                "lastname", contact.lastname,
                "email", contact.email,
                "phone", contact.phone).await()

        return contact!!.id!!
    }
    override suspend fun deleteContact(iduser: String, contact: ContactEntity) : String {
        db.collection("users").document(iduser).collection("contactsperson")
            .document(contact!!.id!!).delete()

        return contact!!.id!!
    }
}

