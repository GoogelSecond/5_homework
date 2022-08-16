package com.example.a5_homework

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun openEditScreen(id: String)

    fun openListScreen()
}