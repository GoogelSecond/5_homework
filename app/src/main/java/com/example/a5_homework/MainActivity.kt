package com.example.a5_homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.screens.ContactEditFragment
import com.example.a5_homework.screens.ContactListFragment
import com.example.a5_homework.utils.ContactCreator
import com.example.a5_homework.utils.ContactUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), Navigator, ContactManager {
    private var fragmentEditContact: FragmentContainerView? = null

    private val contacts = mutableListOf<ContactModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentEditContact = findViewById(R.id.fragment_container_edit_contact)

        if (isOnePanelMode()) {
            if (savedInstanceState == null) {
                openListScreen()
            }
        } else {
            popBackstack()
            openListScreen()
        }
    }

    override fun openEditScreenOnePanelMode(id: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace(R.id.fragment_container, ContactEditFragment.newInstance(id))
        }
    }

    override fun openEditScreenTwoPanelMode(id: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container_edit_contact, ContactEditFragment.newInstance(id))
        }
    }

    override fun openListScreen() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, ContactListFragment.newInstance())
        }
    }

    override fun isOnePanelMode(): Boolean {
        return fragmentEditContact == null
    }

    override fun popBackstack() {
        supportFragmentManager.popBackStack()
    }

    override fun clearEditScreenFragment() {
        val editFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container_edit_contact)

        editFragment?.let {
            supportFragmentManager.commit {
                remove(editFragment)
            }
        }
    }

    override fun createContacts(callback: () -> Unit) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ContactCreator.crateContactList(COUNT_CONTACTS).forEach { contact ->
                    ContactUtils.createContact(contact, contentResolver)
                    withContext(Dispatchers.Main) {
                        callback.invoke()
                    }
                }
            }
        }
    }

    override fun deleteContacts(callback: () -> Unit) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                ContactUtils.deleteAllContacts(contacts, contentResolver)
                withContext(Dispatchers.Main) {
                    contacts.clear()
                    callback.invoke()
                }
            }
        }
    }

    override fun updateContactList() {
        contacts.clear()
        val list = ContactUtils.getContacts(contentResolver)
        contacts.addAll(list)
    }

    override fun loadContacts(): List<ContactModel> {
        return contacts.toList()
    }

    override fun updateContact(contactModel: ContactModel) {
        ContactUtils.updateContact(contactModel, contentResolver)
    }

    override fun getContactById(id: String): ContactModel {
        return ContactUtils.getContactById(id, contentResolver)
    }

    override fun deleteContact(id: String) {
        ContactUtils.deleteContact(id, contentResolver)
    }

    companion object {
        private const val COUNT_CONTACTS = 100
    }
}