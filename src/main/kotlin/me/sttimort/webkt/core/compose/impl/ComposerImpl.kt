package me.sttimort.webkt.core.compose.impl

import me.sttimort.webkt.core.compose.Composer
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtHtmlElement
import me.sttimort.webkt.protobuf.WebKtRenderMessageOuterClass.WebKtHtmlElementOrBuilder
import java.util.*

@Suppress("UNCHECKED_CAST")
class ComposerImpl(
    private val recompose: () -> Unit = {}
) : Composer {
    private val groups = Array(size = 0) { EMPTY_GROUP }
    private var parentGroupIndex = -1
    private var currentGroupIndex = -1
    private val parentGroupIndexesStack = Stack<Int>()

    private val slotsArray = Array<Any?>(size = 0) { null }

    private val cache = mutableListOf<Any?>()

    private val inserting: Boolean
        get() = index == cache.size

    private var index = 0
    private var current = WebKtHtmlElement.newBuilder()

    override val elements: List<WebKtHtmlElement>
        get() = current.childList

    override fun reset() {
        index = 0
        current.clearChild()
    }

    fun inRootGroup(block: ComposerImpl.() -> Unit) {
        startRootGroup()
        block(this)
        endRootGroup()
    }

    private fun startRootGroup() {
        val rootGroup = Group(ROOT_GROUP_KEY)
        groups[++currentGroupIndex] = rootGroup
    }

    private fun endRootGroup() {

    }

    fun startGroup(key: Int) {
        val newGroup = Group(key)
        groups[++currentGroupIndex] = newGroup
        parentGroupIndexesStack.push(parentGroupIndex)
        parentGroupIndex = currentGroupIndex
    }

    fun endGroup() {
        parentGroupIndex = parentGroupIndexesStack.pop()
    }


    override fun <T> Composer.memo(vararg inputs: Any?, factory: () -> T): T {
//        var valid = true
        return cache(write = false, factory)
    }

    override fun emit(element: WebKtHtmlElement.Builder, content: () -> Unit) {
        val previous = current
        current = element
        content()
        current = previous
        current.addChild(element.build())
    }

    fun emitElement(element: WebKtHtmlElementOrBuilder, content: () -> Unit) {
        checkElementExpected()

    }

    override fun onRecompose() {
        recompose()
    }

    private fun <T> cache(write: Boolean, factory: () -> T) = when {
        inserting || write -> factory().also { set(it) }
        else -> get() as T
    }

    private fun get(): Any? = cache[index++]

    private fun set(value: Any?) {
        if (inserting) {
            index++
            cache.add(value)
        } else {
            cache.add(index++, value)
        }
    }

   private fun checkElementExpected() {

   }
}

data class Group(
    val key: Int
)

val EMPTY_GROUP = Group(Int.MIN_VALUE)

const val ROOT_GROUP_KEY = 0