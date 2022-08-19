package com.example.a5_homework.model

data class ContactCPModel(
    val id: String = "",
    val imageByteArray: ByteArray? = null,
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactCPModel

        if (id != other.id) return false
        if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageByteArray.contentHashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }
}