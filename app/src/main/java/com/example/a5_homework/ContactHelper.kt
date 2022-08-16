package com.example.a5_homework

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.*
import kotlin.properties.Delegates

object ContactHelper {


    fun getContactList(context: Context): List<ContactModel> {

        val result = mutableListOf<ContactModel>()

        val nameCursor = context.contentResolver.query(
            CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        var id = ""
        var name = ""
        var phoneNumber = ""

        var idIndex by Delegates.notNull<Int>()
        var nameIndex by Delegates.notNull<Int>()
        var numberIndex by Delegates.notNull<Int>()

        nameCursor?.let { cursor ->
            idIndex = cursor.getColumnIndex(CommonDataKinds.Phone.CONTACT_ID)
            nameIndex = cursor.getColumnIndex(CommonDataKinds.Phone.DISPLAY_NAME)
            numberIndex = cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                if (idIndex > 0) {
                    id = cursor.getString(idIndex)
                }
                if (nameIndex > 0) {
                    name = cursor.getString(nameIndex)
                }
                if (numberIndex > 0) {
                    phoneNumber = cursor.getString(numberIndex)
                }

                // TODO: will fix bag
                val spaceIndex = name.trim().indexOf(SEPARATOR)
                result.add(
                    ContactModel(
                        id = id,
                        firstName = name.substring(0, spaceIndex),
                        lastName = name.substring(spaceIndex, name.length),
                        number = phoneNumber
                    )
                )
            }

            cursor.close()
        }
        return result
    }

    private const val SEPARATOR = " "
}