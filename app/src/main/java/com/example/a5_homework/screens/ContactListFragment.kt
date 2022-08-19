package com.example.a5_homework.screens

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.a5_homework.R
import com.example.a5_homework.SingleDialogFragment
import com.example.a5_homework.contactManager
import com.example.a5_homework.databinding.ContactListFragmentBinding
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.navigator
import com.example.a5_homework.screens.recycler.ContactAdapter


class ContactListFragment : Fragment(R.layout.contact_list_fragment) {

    private lateinit var binding: ContactListFragmentBinding

    private lateinit var contactAdapter: ContactAdapter

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
        ::onGotReadAndWriteContactsPermissionsResult
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        lunchPermissionRequest()
        setupRecycler()
    }

    private fun setupButtons() {
        binding.buttonTryAgain.setOnClickListener {
            lunchPermissionRequest()
        }
        binding.buttonDeleteAllContacts.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            contactManager().deleteContacts {
                binding.progressBar.visibility = View.GONE
                updateUi()
            }
        }
        binding.buttonCreateContacts.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            contactManager().createContacts {
                binding.progressBar.visibility = View.GONE
                updateUi()
            }
        }
    }

    private fun lunchPermissionRequest() {
        permissionRequestLauncher.launch(
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        )
    }

    private fun setupRecycler() {
        val recycler = binding.recyclerView
        contactAdapter = ContactAdapter(
            onContactClickListener = { id -> openEditScreen(id) },
            onLongClickListener = { id -> removeContact(id) }
        )
        recycler.adapter = contactAdapter
    }

    private fun onGotReadAndWriteContactsPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onReadAndWriteContactsPermissionsGranted()
        } else {
            setDiscardedState()
        }
    }

    private fun onReadAndWriteContactsPermissionsGranted() {
        updateUi()
    }

    private fun openEditScreen(id: String) {
        if (navigator().isOnePanelMode()) {
            navigator().openEditScreenOnePanelMode(id)
        } else {
            navigator().openEditScreenTwoPanelMode(id)
        }
    }

    private fun removeContact(id: String) {
        SingleDialogFragment.show(parentFragmentManager)
        SingleDialogFragment.setupListener(parentFragmentManager, this) { answer ->
            if (answer) {
                contactManager().deleteContact(id)
                navigator().clearEditScreenFragment()
                updateUi()
            }
        }
    }

    private fun updateUi() {
        contactManager().updateContactList()
        val contactList = contactManager().loadContacts()
        checkEmptyListContacts(contactList)
    }

    private fun checkEmptyListContacts(contactList: List<ContactModel>) {
        if (contactList.isEmpty()) {
            setEmptyListState()
        } else {
            setDefaultState()
            contactAdapter.submitList(contactList)
        }
    }

    private fun setDiscardedState() {
        binding.tvEmptyListMessage.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.buttonTryAgain.visibility = View.VISIBLE
        binding.buttonCreateContacts.visibility = View.GONE
        binding.buttonDeleteAllContacts.visibility = View.GONE
    }

    private fun setDefaultState() {
        binding.tvEmptyListMessage.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.buttonTryAgain.visibility = View.GONE
        binding.buttonCreateContacts.visibility = View.GONE
        binding.buttonDeleteAllContacts.visibility = View.VISIBLE
    }

    private fun setEmptyListState() {
        binding.tvEmptyListMessage.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.buttonTryAgain.visibility = View.VISIBLE
        binding.buttonCreateContacts.visibility = View.VISIBLE
        binding.buttonDeleteAllContacts.visibility = View.GONE
    }

    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}
