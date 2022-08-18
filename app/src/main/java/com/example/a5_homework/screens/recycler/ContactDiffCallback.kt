package com.example.a5_homework.screens.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.a5_homework.model.ContactModel

class ContactDiffCallback: DiffUtil.ItemCallback<ContactModel>() {
    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem
    }
}