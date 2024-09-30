package ch.bytecraft.parser.state

import ch.bytecraft.linked_stack.LinkedStack

internal class Delta private constructor(
    private val tokensBefore: LinkedStack.Node<Token>?,
    private val tokensAfter: LinkedStack.Node<Token>?,
    private val newStartOfToken: Int?,
    private val newEndOfToken: Int?,
    private val newIndex: Int,
) {
    private val tokens by lazy {
        LinkedStack(tokensAfter)
            .peekUntil(tokensBefore)
            .apply { reverse() }
    }

    constructor(
        tokensBefore: LinkedStack.Node<Token>?,
        state: State,
    ) : this(
        tokensBefore,
        state.tokens.head,
        state.startsOfToken.peekOrNull(),
        state.endsOfToken.peekOrNull(),
        state.index,
    )

    fun applyTo(state: State) {
        tokens.forEach(state.tokens::push)
        newStartOfToken?.let { state.startsOfToken.apply { pop(); push(it) } }
        newEndOfToken?.let { state.endsOfToken.apply { pop(); push(it) } }
        state.index = newIndex
    }
}