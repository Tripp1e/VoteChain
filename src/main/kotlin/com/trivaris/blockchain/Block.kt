package com.trivaris.blockchain

import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Block(
    val data: String,
    val previousHash: String,
    var hash: String = "",
    val timestamp: Long = Date().time,
    var nonce: Int = 0,
    val difficulty: Int = BlockChain.DIFFICULTY
) {
    init {
        hash = calculateHash()
    }

    fun calculateHash(): String {
        val toEncode = previousHash + timestamp.toString() + nonce.toString() + data
        return StringUtil.applySha256(toEncode)
    }

    fun mineBlock() {
        val target = "0".repeat(difficulty)
        while (hash.substring(0, difficulty) != target) {
            nonce++
            hash = calculateHash()
        }
        println("Block mined!! : $hash")
    }
}
