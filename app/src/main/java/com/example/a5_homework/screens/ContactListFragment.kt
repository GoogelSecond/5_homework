package com.example.a5_homework.screens

import android.Manifest
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.a5_homework.*
import com.example.a5_homework.databinding.ContactListFragmentBinding
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.screens.recycler.ContactAdapter


class ContactListFragment : Fragment(R.layout.contact_list_fragment) {

    private var _binding: ContactListFragmentBinding? = null
    private val binding: ContactListFragmentBinding
        get() = _binding!!

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
        _binding = ContactListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        lunchPermissionRequest()
        setupRecycler()
        setupSearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun setupButtons() {
        with(binding) {

            buttonTryAgain.setOnClickListener {
                lunchPermissionRequest()
            }

            buttonDeleteAllContacts.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                interfaceBlocker.visibility = View.VISIBLE

                contactManager().deleteContacts {
                    progressBar.visibility = View.GONE
                    interfaceBlocker.visibility = View.GONE
                    updateUi()
                }
            }

            buttonCreateContacts.setOnClickListener {
                progressBar.visibility = View.VISIBLE
                interfaceBlocker.visibility = View.VISIBLE

                contactManager().createContacts {
                    progressBar.visibility = View.GONE
                    interfaceBlocker.visibility = View.GONE
                    contactManager().updateContactList()
                    updateUi()
                }
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

        val verticalMargin =
            requireContext().resources.getDimension(R.dimen.item_decorator_vertical_margin).toInt()

        val horizontalMargin =
            requireContext().resources.getDimension(R.dimen.item_decorator_horizontal_margin)
                .toInt()

        val itemDecorator = MarginItemDecorator(verticalMargin, horizontalMargin)

        recycler.addItemDecoration(itemDecorator)
    }

    private fun setupSearch() {
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            contactManager().searchContacts(text.toString()) { isEmpty ->
                if (isEmpty) {
                    setEmptyResultState()
                } else {
                    setDefaultState()
                }
            }
            contactAdapter.submitList(contactManager().loadContacts())
        }
    }

    private fun onGotReadAndWriteContactsPermissionsResult(grantResults: Map<String, Boolean>) {
        if (grantResults.entries.all { it.value }) {
            onReadAndWriteContactsPermissionsGranted()
        } else {
            setDiscardedState()
        }
    }

    private fun onReadAndWriteContactsPermissionsGranted() {
        contactManager().updateContactList()
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
                contactManager().updateContactList()
                updateUi()
            }
        }
    }

    private fun updateUi() {
        checkEmptyListContacts(contactManager().loadContacts())
    }

    private fun checkEmptyListContacts(contactList: List<ContactModel>) {
        if (contactList.isEmpty()) {
            setEmptyListState()
        } else {
            setDefaultState()
        }
        contactAdapter.submitList(contactList)
    }

    private fun setEmptyResultState() {
        val text = getText(R.string.contact_list_is_empty_result_message)
        with(binding) {
            tvEmptyListMessage.text = text
            tvEmptyListMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            buttonTryAgain.visibility = View.GONE
            buttonCreateContacts.visibility = View.GONE
            buttonDeleteAllContacts.visibility = View.GONE

            etSearch.visibility = View.VISIBLE
        }
    }

    private fun setDiscardedState() {
        with(binding) {
            tvEmptyListMessage.visibility = View.GONE
            recyclerView.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            buttonCreateContacts.visibility = View.GONE
            buttonDeleteAllContacts.visibility = View.GONE

            etSearch.visibility = View.GONE
        }
    }

    private fun setDefaultState() {
        with(binding) {
            tvEmptyListMessage.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            buttonCreateContacts.visibility = View.GONE
            buttonDeleteAllContacts.visibility = View.VISIBLE

            etSearch.visibility = View.VISIBLE
        }
    }

    private fun setEmptyListState() {
        val text = getText(R.string.contact_list_is_empty_message)
        with(binding) {
            tvEmptyListMessage.text = text
            tvEmptyListMessage.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            buttonCreateContacts.visibility = View.VISIBLE
            buttonDeleteAllContacts.visibility = View.GONE

            etSearch.visibility = View.GONE
        }
    }

    companion object {
        fun newInstance(): ContactListFragment {
            return ContactListFragment()
        }
    }
}
