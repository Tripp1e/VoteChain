package com.trivaris.blockchain

import java.security.MessageDigest

object StringUtil {
    fun applySha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { String.format("%02x", it) }
    }
}
