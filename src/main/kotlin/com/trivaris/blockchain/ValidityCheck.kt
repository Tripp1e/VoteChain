package com.trivaris.blockchain

import com.trivaris.blockchain.BlockChain.blockchain

object ValidityCheck {

    fun checkBlockWithLatest(block: Block): Boolean { return checkBlockWithPrevious(block, blockchain.last()) }
    fun checkBlockWithPrevious(currentBlock: Block, previousBlock: Block): Boolean {
        if (!isBlockMined(currentBlock))                           return false
        if (!doCurrentHashesMatch(currentBlock))                   return false
        if (!doPreviousHashesMatch(currentBlock, previousBlock))   return false

        return true
    }

    fun checkBlockChain(): Boolean {
        var currentBlock: Block?
        var previousBlock: Block?

        for (i in 1 until blockchain.size) {
            currentBlock = blockchain[i]
            previousBlock = blockchain[i - 1]
            if (!checkBlockWithPrevious(currentBlock, previousBlock)) return false
        }
        return true
    }

    private fun doCurrentHashesMatch(block: Block): Boolean {
        if (block.hash == block.calculateHash()) return true
        println("Current Hashes do not match")
        return false
    }

    private fun isBlockMined(block: Block): Boolean {
        if (block.hash.substring(0, block.difficulty) == "0".repeat(block.difficulty)) return true
        println("Block was not Mined")
        return false
    }

    private fun doPreviousHashesMatch(currBlock: Block, prevBlock: Block): Boolean {
        if (prevBlock.hash == currBlock.previousHash) return true
        println("Previous Hashes do not match")
        return false
    }

}