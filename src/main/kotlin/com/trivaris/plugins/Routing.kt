package com.trivaris.plugins

import com.trivaris.blockchain.BlockChain
import com.trivaris.blockchain.ValidityCheck
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        staticFiles("/", File("resources/static"))

        post("/submit") {
            val params = call.receiveParameters()
            BlockChain.addBlock(params["candidates"].toString())
            ValidityCheck.checkBlockChain()
            BlockChain.printBlockChain()
            call.respondRedirect("/")
        }

    }
}