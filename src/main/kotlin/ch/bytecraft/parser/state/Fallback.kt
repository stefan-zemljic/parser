package ch.bytecraft.parser.state

import ch.bytecraft.linked_stack.LinkedStack

internal class Fallback private constructor(
    val tokenStack: LinkedStack.Node<Token>?,
    val startsOfToken: LinkedStack.Node<Int>?,
    val endsOfToken: LinkedStack.Node<Int>?,
    val index: Int,
) {
    constructor(state: State) : this(
        state.tokens.head,
        state.startsOfToken.head,
        state.endsOfToken.head,
        state.index,
    )

    fun applyTo(state: State) {
        state.tokens.head = tokenStack
        state.startsOfToken.head = startsOfToken
        state.endsOfToken.head = endsOfToken
        state.index = index
    }
}