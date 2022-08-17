package com.example.a5_homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a5_homework.databinding.ContactEditFragmentBinding
import com.google.android.material.snackbar.Snackbar


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
            Snackbar.make(binding.root, id, Snackbar.LENGTH_LONG).show()

        }

        binding.buttonSave.setOnClickListener {
            navigator().popBackstack()
            navigator().openListScreen()
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

