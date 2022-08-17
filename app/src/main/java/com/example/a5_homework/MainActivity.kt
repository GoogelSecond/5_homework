package com.example.a5_homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.commit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), Navigator {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            openListScreen()
        }
    }

    override fun openEditScreen(id: String) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(null)
            replace(R.id.fragment_container, ContactEditFragment.newInstance(id))
        }
    }

    override fun openListScreen() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, ContactListFragment.newInstance())
        }
    }

    override fun popBackstack() {
       supportFragmentManager.popBackStack()
    }
}