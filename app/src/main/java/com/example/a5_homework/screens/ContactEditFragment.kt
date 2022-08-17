package com.example.a5_homework.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a5_homework.ContactHelper
import com.example.a5_homework.R
import com.example.a5_homework.databinding.ContactEditFragmentBinding
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.navigator


class ContactEditFragment : Fragment(R.layout.contact_list_fragment) {

    private lateinit var binding: ContactEditFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactEditFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments()
        val id = args.getString(KEY_ID)

        id?.let {
            val contactModel = ContactHelper.getContactById(it, requireContext().contentResolver)
            binding.etFirstName.setText(contactModel.firstName)
            binding.etLastName.setText(contactModel.lastName)
            binding.etPhoneNumber.setText(contactModel.number)
        }

        binding.buttonSave.setOnClickListener {
            id?.let { id ->
                ContactHelper.updateContact(
                    ContactModel(
                        id = id,
                        firstName = binding.etFirstName.text.toString(),
                        lastName = binding.etLastName.text.toString(),
                        number = binding.etPhoneNumber.text.toString()
                    ), requireContext().contentResolver
                )
                navigator().popBackstack()
                navigator().openListScreen()
            }
        }
    }

    companion object {
        private const val KEY_ID = "key id"

        fun newInstance(id: String): ContactEditFragment {
            return ContactEditFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_ID, id)
                }
            }
        }
    }
}

