package com.trivaris.blockchain

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    BlockChain.addTestBlocks()
    BlockChain.printBlockChain()
}

object BlockChain {

    val blockchain = arrayListOf<Block>(Block("data", "0"))
    const val DIFFICULTY = 5

    fun printBlockChain() {

        println("\nThe blockchain is valid: ${ValidityCheck.checkBlockChain()}")

        val votingChainJson = Json.encodeToString(blockchain)
        println("\nThe blockchain: ")
        println(votingChainJson)

    }

    fun addTestBlocks() {
        addBlock("Hi, Im the first block")
        addBlock("Yo, Im the second block")
        addBlock("Oy, Im the third block")
    }

    fun addBlock(data: String) {
        val prevHash = if (blockchain.isEmpty()) "0" else blockchain.last().hash
        val block = Block(data, prevHash)
        addBlock(block)
    }
    fun addBlock(block: Block) {
        block.mineBlock()
        if (!ValidityCheck.checkBlock(block)) return
        blockchain.add(block)
    }

}