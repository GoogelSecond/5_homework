package com.example.a5_homework

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.a5_homework.databinding.ContactListFragmentBinding
import com.example.a5_homework.recycler.ContactAdapter
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

        permissionRequestLauncher.launch(
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
        )

        setupRecycler()
    }

    private fun setupRecycler() {
        val recycler = binding.recyclerView
        contactAdapter = ContactAdapter { id ->
//            Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG).show()
            navigator().openEditScreen(id)
        }
        recycler.adapter = contactAdapter
    }

    private fun onGotReadAndWriteContactsPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onReadAndWriteContactsPermissionsGranted()
        }
    }

    private fun onReadAndWriteContactsPermissionsGranted() {
        val contactList = ContactHelper.getContacts(requireContext().contentResolver)
        contactAdapter.submitList(contactList)
    }

    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}

