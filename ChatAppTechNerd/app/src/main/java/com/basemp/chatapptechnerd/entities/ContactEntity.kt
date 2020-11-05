package com.basemp.chatapptechnerd.entities

data class ContactEntity (
    var id: String? = "",
    var firstname: String? = "",
    var lastname: String? = "",
    var email: String? = "",
    var phone: String? = ""
)
data class ListContactEntity (
    var contact: List<ContactEntity>?
)