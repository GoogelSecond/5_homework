package com.example.a5_homework.screens.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.databinding.ContactItemBinding

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ContactItemBinding.bind(view)
    val rootView = binding.root

    fun bind(contactModel: ContactModel) {
        binding.tvFirstName.text = contactModel.firstName
        binding.tvLastName.text = contactModel.lastName
        binding.tvPhoneNumber.text = contactModel.number
    }
}