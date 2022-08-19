package com.example.a5_homework

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.a5_homework.screens.ContactEditFragment
import com.example.a5_homework.screens.ContactListFragment

class MainActivity : AppCompatActivity(), Navigator {
    private var fragmentEditContact: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fragmentEditContact = findViewById(R.id.fragment_container_edit_contact)

        if (isOnePanelMode()) {
            if (savedInstanceState == null) {
                openListScreen()
            }
        } else {
            supportFragmentManager.popBackStack(
                TRANSITION_NAME_EDIT_ONE_PANEL_MODE,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
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

    companion object {
        private const val TRANSITION_NAME_EDIT_ONE_PANEL_MODE = "edit"
    }
}