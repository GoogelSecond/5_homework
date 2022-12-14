package com.example.a5_homework.screens.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.a5_homework.model.ContactModel
import com.example.a5_homework.R

class ContactAdapter(
    private val onContactClickListener: (id: String) -> Unit,
    private val onLongClickListener: (id: String) -> Unit
) :
    ListAdapter<ContactModel, ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        val holder = ContactViewHolder(view)

        val rootView = holder.rootView

        rootView.setOnLongClickListener {
            val position = holder.adapterPosition
            if (position != NO_POSITION) {
                val id = currentList[position].id
                onLongClickListener.invoke(id)
            }
            true
        }

        rootView.setOnClickListener {
            val position = holder.adapterPosition
            if (position != NO_POSITION) {
                val id = currentList[position].id
                onContactClickListener.invoke(id)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }

    companion object {
        private const val NO_POSITION = -1
    }
}