package com.example.a5_homework

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun openEditScreenOnePanelMode(id: String)

    fun openEditScreenTwoPanelMode(id: String)

    fun openListScreen()

    fun isOnePanelMode(): Boolean

    fun popBackstack()

    fun clearEditScreenFragment()
}