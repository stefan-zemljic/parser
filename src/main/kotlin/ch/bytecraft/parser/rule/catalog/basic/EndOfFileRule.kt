package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal object EndOfFileRule : ParseRule() {
    override fun parse(state: State): Boolean {
        return state.index == state.source.length
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // No children
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append('$')
    }
}