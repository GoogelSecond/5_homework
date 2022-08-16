package com.example.a5_homework

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.a5_homework.databinding.ContactListFragmentBinding


class ContactListFragment : Fragment(R.layout.contact_list_fragment) {

    private lateinit var binding: ContactListFragmentBinding

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

        binding.button.setOnClickListener {
            permissionRequestLauncher.launch(
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
            )
        }
    }

    private fun onGotReadAndWriteContactsPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onReadAndWriteContactsPermissionsGranted()
        }
    }

    private fun onReadAndWriteContactsPermissionsGranted() {
        val contactList = ContactHelper.getContacts(requireContext().contentResolver)
    }
}

