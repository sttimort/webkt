package me.sttimort.webkt.core

import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.WebsocketServerTransport
import kotlinx.coroutines.reactive.awaitLast

class WebKt(
    private val configuration: WebKtConfiguration
) {
    suspend fun start() {
        RSocketServer.create()
            .bind(configuration.host
                ?.let { WebsocketServerTransport.create(it, configuration.port) }
                ?: WebsocketServerTransport.create(configuration.port)
            )
            .awaitLast()
    }
}