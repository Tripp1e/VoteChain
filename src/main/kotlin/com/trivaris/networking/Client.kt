package com.trivaris.networking

import com.trivaris.blockchain.Block
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json

object Client {

    val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun dispatchAddRequest(body: Block, url: String): HttpResponse {
        val response = client.post("http://${url}:8080/add") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response
    }

}