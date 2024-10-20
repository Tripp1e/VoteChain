package com.trivaris.networking

import com.trivaris.blockchain.*
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.origin
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

val localHosts = mapOf("v4" to "127.0.0.1","v6" to "::1")
// TODO: Update this so that it gets the IPs from a central server and/or other nodes
val exampleIPList = arrayOf("127.0.0.1")

var hasVoted = false

fun Application.configureRouting() {

    routing {

        get("/") {
            if (!hasVoted) call.respondFile(File("resources/static/index.html"))
            else call.respond(HttpStatusCode.Forbidden, "You already voted")
        }

        //Submit Block to local Blockchain from localhost
        post("/submit") {
            if (hasVoted) call.respond(HttpStatusCode.Forbidden, "You already voted") else hasVoted = true

            val clientIp = call.request.origin.remoteHost
            if (clientIp !in localHosts.values) return@post call.respond(HttpStatusCode.Forbidden, "Access denied")

            val params = call.receiveParameters()
            val newBlock = Block(params["candidates"] as String)
            newBlock.mine()

            for (ip in exampleIPList) {
                println(Client.dispatchAddRequest(newBlock, ip))
            }

            call.respondRedirect("/")
        }

        //Get Block from different node
        post("/add") {
            val block = call.receive<Block>()
            if (!ValidityCheck.checkBlockWithLatest(block)) return@post call.respond(HttpStatusCode.BadRequest, "Invalid Block")

            BlockChain.addBlock(block)
            call.respond(HttpStatusCode.OK, "Added Block")
        }

        get("/debug") {
            val newBlock = Block("DebugBlock")
            Client.dispatchAddRequest(newBlock, localHosts["v4"]!!)
        }

    }
}

fun Application.configureSerialization() {

    install(ContentNegotiation) {
        json()
    }

}