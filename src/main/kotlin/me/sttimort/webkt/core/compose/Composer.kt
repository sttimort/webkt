package me.sttimort.webkt.core.compose

import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtHtmlElement

interface Composer {
    val elements: List<WebKtHtmlElement>

    fun reset()
    fun <T> Composer.memo(vararg inputs: Any?, factory: () -> T): T
    fun emit(element: WebKtHtmlElement.Builder, content: () -> Unit = {})
    fun onRecompose()
}