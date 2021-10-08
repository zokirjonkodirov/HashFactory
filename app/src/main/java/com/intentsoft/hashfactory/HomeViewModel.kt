package com.intentsoft.hashfactory

import androidx.lifecycle.ViewModel
import java.security.MessageDigest

/**
 * @author user
 * @date 07.10.2021
 */
class HomeViewModel: ViewModel() {

    fun getHash(plainText: String, algorithm: String): String {
        val bytes = MessageDigest.getInstance(algorithm).digest(plainText.toByteArray())

        return toHex(bytes)
    }

    private fun toHex(byteArray: ByteArray): String {
        return byteArray.joinToString("") { "%02x".format(it) }
    }
}