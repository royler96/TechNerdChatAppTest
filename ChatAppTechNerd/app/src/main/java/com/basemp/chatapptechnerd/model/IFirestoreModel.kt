package com.basemp.chatapptechnerd.model

import com.basemp.chatapptechnerd.entities.ContactEntity
import com.basemp.chatapptechnerd.entities.UserEntity

interface IFirestoreModel {
    fun getUser(id: String) : UserEntity?;
    suspend fun  getUserEmail(email: String): UserEntity?
    suspend fun saveUser(user: UserEntity) : String
    suspend fun getContactsByUser(idUser: String) : List<ContactEntity>
    suspend fun getContactsByUserTextSearch(coduser: String, textSearch: String): List<ContactEntity>
    suspend fun saveContact(iduser: String, contact: ContactEntity) : String
    suspend fun updateContact(iduser: String, contact: ContactEntity) : String
    suspend fun deleteContact(iduser: String, contact: ContactEntity) : String
}