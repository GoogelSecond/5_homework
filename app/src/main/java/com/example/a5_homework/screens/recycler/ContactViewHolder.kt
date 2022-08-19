package com.example.a5_homework.screens.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.a5_homework.R
import com.example.a5_homework.databinding.ContactItemBinding
import com.example.a5_homework.model.ContactModel

class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ContactItemBinding.bind(view)
    val rootView = binding.root

    fun bind(contactModel: ContactModel) {
        setImage(contactModel)
        binding.tvFirstName.text = contactModel.firstName
        binding.tvLastName.text = contactModel.lastName
        binding.tvPhoneNumber.text = contactModel.phoneNumber
    }

    private fun setImage(contactModel: ContactModel) {
        Glide.with(binding.root)
            .load(contactModel.imageUri)
            .override(200, 200)
            .error(
                Glide.with(binding.root)
                    .load(R.drawable.ic_launcher_background)
                    .apply(RequestOptions.circleCropTransform())
            )
            .apply(RequestOptions.circleCropTransform())
            .into(binding.ivContactImage)
    }
}