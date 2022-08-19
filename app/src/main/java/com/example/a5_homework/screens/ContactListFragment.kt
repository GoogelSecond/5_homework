package com.example.a5_homework.screens

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.a5_homework.ContactHelper
import com.example.a5_homework.R
import com.example.a5_homework.SingleDialogFragment
import com.example.a5_homework.databinding.ContactListFragmentBinding
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.navigator
import com.example.a5_homework.screens.recycler.ContactAdapter
import com.google.android.material.snackbar.Snackbar


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

        binding.buttonTryAgain.setOnClickListener {
            lunchPermissionRequest()
        }
        lunchPermissionRequest()
        setupRecycler()
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
            onLongClickListener = { id -> removeContact(id) })
        recycler.adapter = contactAdapter
    }

    private fun onGotReadAndWriteContactsPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onReadAndWriteContactsPermissionsGranted()
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
                ContactHelper.deleteContact(id, requireContext().contentResolver)
                updateUi()
            }
        }
    }

    private fun updateUi() {
        val contactList = ContactHelper.getContacts(requireContext().contentResolver)
        checkEmptyListContacts(contactList)
    }

    private fun checkEmptyListContacts(contactList: List<ContactModel>) {
        if (contactList.isEmpty()) {
            binding.tvEmptyListMessage.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
            binding.buttonTryAgain.visibility = View.VISIBLE
        } else {
            binding.tvEmptyListMessage.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.buttonTryAgain.visibility = View.GONE
            contactAdapter.submitList(contactList)
        }
    }

    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}
