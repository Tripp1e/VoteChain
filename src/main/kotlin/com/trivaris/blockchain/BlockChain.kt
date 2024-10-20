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
    val json = Json { prettyPrint = true }

    fun printBlockChain() {

        println("\nThe blockchain is valid: ${ValidityCheck.checkBlockChain()}")

        val votingChainJson = json.encodeToString(blockchain)
        println("\nThe blockchain: ")
        println(votingChainJson)

    }

    fun addTestBlocks() {
        addBlock(Block("Hi, Im the first block"))
        addBlock(Block("Yo, Im the second block"))
        addBlock(Block("Oy, Im the third block"))
    }

    fun addBlock(block: Block) {
        block.mine()
        if (!ValidityCheck.checkBlockWithLatest(block)) return
        blockchain.add(block)
    }

}