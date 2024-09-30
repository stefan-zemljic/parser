package ch.bytecraft.parser.rule.catalog.token

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal object TokenStartRule : ParseRule() {
    override fun parse(state: State): Boolean {
        val current = state.startsOfToken
        if (current.peek() != Int.MAX_VALUE) return true
        current.pop()
        current.push(state.index)
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // no children
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append("{")
    }
}