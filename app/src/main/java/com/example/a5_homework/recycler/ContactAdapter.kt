package com.example.a5_homework.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.a5_homework.ContactModel
import com.example.a5_homework.R

class ContactAdapter(private val onContactClickListener: (id: String) -> Unit) :
    ListAdapter<ContactModel, ContactViewHolder>(ContactDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        val holder = ContactViewHolder(view)

        val rootView = holder.rootView

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