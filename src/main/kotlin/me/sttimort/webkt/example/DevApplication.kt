import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketServer
import io.rsocket.transport.netty.server.WebsocketServerTransport
import reactor.core.publisher.Mono

fun main() {
    println("main")

    RSocketServer.create()
        .acceptor(SocketAcceptor.forFireAndForget {
            println("$it")
            Mono.empty()
        })
        .bindNow(WebsocketServerTransport.create(7000))

    Thread.sleep(1000000)
}