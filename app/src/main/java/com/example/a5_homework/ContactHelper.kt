package com.example.a5_homework

import android.content.ContentResolver
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds

object ContactHelper {

    fun getContacts(contentResolver: ContentResolver): List<ContactModel> {
        val uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            CommonDataKinds.StructuredName.CONTACT_ID,
            CommonDataKinds.StructuredName.GIVEN_NAME,
            CommonDataKinds.StructuredName.FAMILY_NAME
        )
        val selection = "${ContactsContract.Data.MIMETYPE}=?"
        val selectionArgs = arrayOf(CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)

        val nameCursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        val result = mutableListOf<ContactModel>()

        nameCursor?.let { cursor ->
            var id: String
            var firstName: String
            var lastName: String
            var phoneNumber: String

            while (cursor.moveToNext()) {
                id = cursor.getString(0)
                firstName = cursor.getString(1)
                lastName = cursor.getString(2)

                phoneNumber = getPhoneNumber(id, contentResolver)

                result.add(
                    ContactModel(
                        id = id,
                        firstName = firstName,
                        lastName = lastName,
                        number = phoneNumber
                    )
                )
            }

            cursor.close()
        }
        return result
    }

    private fun getPhoneNumber(id: String, contentResolver: ContentResolver): String {

        val uri = CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(CommonDataKinds.Phone.NUMBER)
        val selection = "${CommonDataKinds.Phone.CONTACT_ID}=?"
        val selectionArgs = arrayOf(id)

        val phoneCursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        phoneCursor?.let { cursor ->
            if (cursor.moveToNext()) {
                return cursor.getString(0)
            }
            cursor.close()
        }

        return EMPTY_STRING
    }

    private const val EMPTY_STRING = ""
}