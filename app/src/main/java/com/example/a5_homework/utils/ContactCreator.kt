package com.example.a5_homework.utils

import com.example.a5_homework.model.ContactCPModel
import com.mooveit.library.Fakeit
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

object ContactCreator {

    private const val PICTURES_URL = "https://picsum.photos/200/200"

    fun crateContactList(count: Int): List<ContactCPModel> {
        Fakeit.initWithLocale(Locale.ENGLISH)
        val result = mutableListOf<ContactCPModel>()
        for (i in 0 until count) {
            result.add(
                ContactCPModel(
                    imageByteArray = getImageBytes(PICTURES_URL),
                    firstName = Fakeit.name().firstName(),
                    lastName = Fakeit.name().lastName(),
                    phoneNumber = Fakeit.phone().formats()
                )
            )
        }
        return result
    }

    @Throws(IOException::class)
    private fun getImageBytes(imageUrl: String): ByteArray? {
        val url = URL(imageUrl)
        val output = ByteArrayOutputStream()
        try {
            url.openStream().use { stream ->
                val buffer = ByteArray(4096)
                while (true) {
                    val bytesRead: Int = stream.read(buffer)
                    if (bytesRead < 0) {
                        break
                    }
                    output.write(buffer, 0, bytesRead)
                }
            }
        } catch (e: IOException) {
            return null
        }

        return output.toByteArray()
    }
}