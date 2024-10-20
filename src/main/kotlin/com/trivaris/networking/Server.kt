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

val localHosts = mapOf("v4" to "127.0.0.1","v6" to "::1")
// TODO: Update this so that it gets the IPs from a central server and/or other nodes
val exampleIPList = arrayOf("127.0.0.1")

fun Application.configureRouting() {

    routing {
        staticFiles("/", File("resources/static"))

        post("/submit") {
            val clientIp = call.request.origin.remoteHost
            if (clientIp in localHosts) return@post call.respond(HttpStatusCode.Forbidden, "Access denied")


            val params = call.receiveParameters()
            val newBlock = Block(params["candidates"].toString())

            for(ip in exampleIPList) Client.dispatchPostRequest(newBlock, ip)

            call.respondRedirect("/")
        }

        post("/add") {
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

        get("/debug") {
            val newBlock = Block("DebugBlock")
            Client.dispatchPostRequest(newBlock, localHosts["v4"]!!)
        }

    }
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}