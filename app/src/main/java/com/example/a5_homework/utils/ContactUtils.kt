package com.example.a5_homework.utils

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import com.example.a5_homework.model.ContactCPModel
import com.example.a5_homework.model.ContactModel

object ContactUtils {

    fun createContact(contactModel: ContactCPModel, contentResolver: ContentResolver) {
        val ops = ArrayList<ContentProviderOperation>()
        val rawContactInsertIndex = ops.size

        ops.add(
            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Phone.NUMBER, contactModel.phoneNumber)
                .withValue(ContactsContract.Data.MIMETYPE, CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.TYPE_MOBILE)
                .build()
        )

        ops.add(
            ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(
                    ContactsContract.Data.MIMETYPE,
                    CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                )
                .withValue(CommonDataKinds.StructuredName.GIVEN_NAME, contactModel.firstName)
                .withValue(CommonDataKinds.StructuredName.FAMILY_NAME, contactModel.lastName)
                .build()
        )

        contactModel.imageByteArray?.let { imageByteArray ->
            ops.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex
                    )
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                    )
                    .withValue(CommonDataKinds.Photo.PHOTO, imageByteArray)
                    .build()
            )
        }

        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

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
                        imageUri = getPictureUri(id),
                        firstName = checkBlankString(firstName),
                        lastName = checkBlankString(lastName),
                        phoneNumber = checkBlankString(phoneNumber)
                    )
                )
            }

            cursor.close()
        }
        return result
    }

    fun getContactById(id: String, contentResolver: ContentResolver): ContactModel {
        val uri = ContactsContract.Data.CONTENT_URI
        val projection = arrayOf(
            CommonDataKinds.StructuredName.GIVEN_NAME,
            CommonDataKinds.StructuredName.FAMILY_NAME
        )
        val selection =
            "${ContactsContract.Data.CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=?"
        val selectionArgs = arrayOf(id, CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)

        val nameCursor = contentResolver.query(uri, projection, selection, selectionArgs, null)

        var contactModel = ContactModel()

        nameCursor?.let { cursor ->
            if (cursor.moveToNext()) {
                val firstName = cursor.getString(0)
                val lastName = cursor.getString(1)

                val phoneNumber = getPhoneNumber(id, contentResolver)

                contactModel = ContactModel(
                    id = id,
                    imageUri = getPictureUri(id),
                    firstName = checkBlankString(firstName),
                    lastName = checkBlankString(lastName),
                    phoneNumber = checkBlankString(phoneNumber)
                )
            }

            cursor.close()
        }
        return contactModel
    }

    fun updateContact(contactModel: ContactModel, contentResolver: ContentResolver) {

        val ops = ArrayList<ContentProviderOperation>()

        val phoneSelection =
            "${ContactsContract.Data.CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=? AND ${CommonDataKinds.Phone.TYPE}=?"
        val nameSelection =
            "${ContactsContract.Data.CONTACT_ID}=? AND ${ContactsContract.Data.MIMETYPE}=?"

        val phoneSelectionArgs = arrayOf(
            contactModel.id,
            CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
            CommonDataKinds.Phone.TYPE_MOBILE.toString()
        )
        val nameSelectionArgs = arrayOf(
            contactModel.id,
            CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
        )
        ops.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(phoneSelection, phoneSelectionArgs)
                .withValue(CommonDataKinds.Phone.NUMBER, contactModel.phoneNumber)
                .build()
        )
        ops.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(nameSelection, nameSelectionArgs)
                .withValue(CommonDataKinds.StructuredName.GIVEN_NAME, contactModel.firstName)
                .build()
        )
        ops.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(nameSelection, nameSelectionArgs)
                .withValue(CommonDataKinds.StructuredName.FAMILY_NAME, contactModel.lastName)
                .build()
        )

        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    fun deleteAllContacts(list: List<ContactModel>, contentResolver: ContentResolver) {
        list.forEach {
            deleteContact(it.id, contentResolver)
        }
    }

    fun deleteContact(id: String, contentResolver: ContentResolver) {
        val ops = ArrayList<ContentProviderOperation>()
        val selection = "${ContactsContract.RawContacts.CONTACT_ID}=?"
        val selectionArgs = arrayOf(id)
        ops.add(
            ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(selection, selectionArgs)
                .build()
        )
        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    private fun checkBlankString(string: String): String {
        if (string.isNotBlank()) {
            return string
        }
        return BANK_STRING_MESSAGE
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

    private fun getPictureUri(id: String): Uri {
        return Uri.withAppendedPath(
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id.toLong()),
            ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
        )
    }

    private const val EMPTY_STRING = ""
    private const val BANK_STRING_MESSAGE = "is absent"
}