package com.example.a5_homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.a5_homework.screens.ContactEditFragment
import com.example.a5_homework.screens.ContactListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), Navigator {
    private var fragmentEditContact: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentEditContact = findViewById(R.id.fragment_container_edit_contact)

        if (isOnePanelMode()) {
            if (savedInstanceState == null) {
                lifecycleScope.launch {
                    val list = withContext(Dispatchers.IO) {
                        ContactCreator.crateContactList()
                    }
//                    ContactHelper.createContact(list[0], contentResolver)

                }
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
            addToBackStack(TRANSITION_NAME_EDIT_ONE_PANEL_MODE)
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

    companion object {
        private const val TRANSITION_NAME_EDIT_ONE_PANEL_MODE = "edit"
    }
}