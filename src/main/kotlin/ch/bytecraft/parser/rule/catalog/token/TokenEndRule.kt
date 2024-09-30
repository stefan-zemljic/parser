package ch.bytecraft.parser.rule.catalog.token

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal object TokenEndRule : ParseRule() {
    override fun parse(state: State): Boolean {
        state.endsOfToken.apply {
            pop()
            push(state.index)
        }
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // no children
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append("}")
    }
}