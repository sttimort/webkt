package me.sttimort.webkt.core.compose

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.sttimort.webkt.core.compose.impl.ComposerImpl
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtHtmlElement
import java.util.concurrent.atomic.AtomicReference

class Page(
    val styles: List<String> = listOf(),
    private val buildPageContent: ComposerImpl.() -> Unit = {},
) {
    private lateinit var composer: ComposerImpl

    fun init(recompose: () -> Unit) {
        composer = ComposerImpl(recompose = recompose)
    }

    fun compose(): List<WebKtHtmlElement> {
        composer.reset()
        composer.inRootGroup(buildPageContent)
        return composer.elements
    }
}

fun ComposerImpl.Text(content: String) {
    emit(
        WebKtHtmlElement.newBuilder()
            .setType(WebKtHtmlElement.Type.TEXT)
            .setText(content)
    )
}

fun ComposerImpl.Container(classes:List<String> = listOf(),contentBuilder: ComposerImpl.() -> Unit) {
    emit(
        WebKtHtmlElement.newBuilder()
            .setType(WebKtHtmlElement.Type.DIV)
            .putAttributes("class", classes.joinToString(separator = " "))
    ) {
        contentBuilder.invoke(this)
    }
}

class Component

fun ComposerImpl.Component(content: Composer.() -> Unit = {}) {
//    startGroup(Component::class.hashCode())

    content()
//    endGroup()
}

fun <T : Any> Composer.observe(
    initialValue: T,
    flow: suspend () -> Flow<T>
): T {
    val valueWrapper = memo { AtomicReference(initialValue) }
    memo {
        println("launching")
        GlobalScope.launch {
            flow().collect {
                valueWrapper.set(it)
                onRecompose()
            }
        }
    }
    return valueWrapper.get()
}