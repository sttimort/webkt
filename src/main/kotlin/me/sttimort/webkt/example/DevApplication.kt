package me.sttimort.webkt.example

import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.WebsocketServerTransport
import io.rsocket.util.ByteBufPayload
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import me.sttimort.webkt.protobuf.MetadataOuterClass.Metadata
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtHtmlElement
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtRenderMessage
import me.sttimort.webkt.util.logger
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun main() = runBlocking {
    println("main")

    val log by logger()

    RSocketServer.create()
        .acceptor { setup, _ ->
            log.debug { "SETUP ${setup.metadataMimeType()} ${setup.dataMimeType()}" }
            when {
                setup.metadataMimeType() != "application/octet-stream" ||
                    setup.dataMimeType() != "application/octet-stream"
                -> Mono.error(IllegalArgumentException("only application/octet-stream is supported"))

                else -> Mono.just(object : RSocket {
                    override fun requestChannel(payloads: Publisher<Payload>): Flux<Payload> {
                        return Flux.create { fluxSink ->
                            Something(fluxSink, Flux.from(payloads)).start()
                        }
                    }
                })
            }
        }
        .bindNow(WebsocketServerTransport.create(7000))

    delay(Duration.INFINITE)
}

private class Something(
    private val fluxSink: FluxSink<Payload>,
    private val payloads: Flux<Payload>
) {
    fun start() {
        payloads
            .doOnNext {
                val metadata = Metadata.parseFrom(it.metadata)
                log.debug { "metadata $metadata ${metadata.type}" }
                log.debug { "data ${it.dataUtf8}" }
            }
            .subscribe()

        val clockPage = ClockPage()
        val render: () -> Unit = {
            runBlocking {
                renderToRoot(fluxSink, clockPage.compose())
            }
        }
        clockPage.init(render)
        render()
    }

    companion object {
        private val log by logger()
    }
}

private fun renderToRoot(fluxSink: FluxSink<Payload>, webKtHtmlElement: List<WebKtHtmlElement>) {
    val metadata = Metadata.newBuilder().setType(Metadata.Type.RENDER).build()
    val data = WebKtRenderMessage.newBuilder().apply {
        action = WebKtRenderMessage.Action.OVERWRITE_CHILDREN
        targetElementId = "web-kt-root"
        addAllElement(webKtHtmlElement)
    }
        .build()

    fluxSink.next(ByteBufPayload.create(data.toByteArray(), metadata.toByteArray()))
}