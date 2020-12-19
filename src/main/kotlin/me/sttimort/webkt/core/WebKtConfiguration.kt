package me.sttimort.webkt.core

private const val DEFAULT_PORT = 7000

data class WebKtConfiguration(
    val host: String? = null,
    val port: Int = DEFAULT_PORT
)