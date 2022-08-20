package com.example.a5_homework

import androidx.fragment.app.Fragment
import com.example.a5_homework.model.ContactModel

fun Fragment.contactManager(): ContactManager {
    return requireActivity() as ContactManager
}

interface ContactManager {

    fun createContacts(callback: () -> Unit)

    fun deleteContacts(callback: () -> Unit)

    fun updateContactList()

    fun loadContacts(): List<ContactModel>

    fun updateContact(contactModel: ContactModel)

    fun getContactById(id: String): ContactModel

    fun deleteContact(id: String)

    fun searchContacts(text: String, isEmptyResult: (Boolean) -> Unit)
}