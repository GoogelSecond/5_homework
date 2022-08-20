package com.example.a5_homework.model

import android.net.Uri

data class ContactModel(
    val id: String = "",
    val imageUri: Uri = Uri.EMPTY,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = ""
)