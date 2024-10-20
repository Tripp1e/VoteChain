package com.trivaris

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import com.trivaris.networking.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}