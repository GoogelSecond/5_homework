package com.example.a5_homework.utils

import com.example.a5_homework.model.ContactCPModel
import com.mooveit.library.Fakeit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

object ContactCreator {

    private const val PICTURES_URL = "https://picsum.photos/200/200"

    init {
        Fakeit.initWithLocale(Locale.ENGLISH)
    }

    suspend fun crateContactList(count: Int): Flow<ContactCPModel> = flow {

        for (i in 0 until count) {
            emit(
                ContactCPModel(
                    imageByteArray = getImageBytes(PICTURES_URL),
                    firstName = Fakeit.name().firstName(),
                    lastName = Fakeit.name().lastName(),
                    phoneNumber = Fakeit.phone().formats()
                )
            )
        }
    }

    @Throws(IOException::class)
    private suspend fun getImageBytes(imageUrl: String): ByteArray? = withContext(Dispatchers.IO) {
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
            return@withContext null
        }

        output.toByteArray()
    }
}