package com.trivaris.networking

import com.trivaris.blockchain.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.origin
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

fun Application.configureRouting() {
    routing {
        staticFiles("/", File("resources/static"))

        get("/debug") {
            val newBlock = Block("DebugBlock", BlockChain.blockchain.last().hash)
            Client.dispatchPostRequest(newBlock, "127.0.0.1")
        }

        post("/submit") {
            val clientIp = call.request.origin.remoteHost
            if (clientIp != "127.0.0.1" && clientIp != "::1") {
                call.respond(HttpStatusCode.Forbidden, "Access denied")
                return@post
            }

            val params = call.receiveParameters()
            val newBlock = Block(params["candidates"].toString())

            Client.dispatchPostRequest(newBlock, "127.0.0.1")
            BlockChain.addBlock(newBlock)

            call.respondRedirect("/")
        }

        post("/add") {
            val clientIp = call.request.origin.remoteHost
            if (clientIp == "127.0.0.1") {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@post
            }
            val block = call.receive<Block>()

            if (!ValidityCheck.checkBlock(block)) {
                call.respond(HttpStatusCode.BadRequest, "Invalid Block")
                return@post
            }

            BlockChain.addBlock(block)
            ValidityCheck.checkBlockChain()
            BlockChain.printBlockChain()

            call.respond(HttpStatusCode.OK, "Added Block")
        }

    }
}