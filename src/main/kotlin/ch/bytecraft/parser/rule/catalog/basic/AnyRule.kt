package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal object AnyRule : ParseRule() {
    override fun parse(state: State): Boolean {
        if (state.index == state.source.length) return false
        val codePoint = state.source.codePointAt(state.index)
        state.index += Character.charCount(codePoint)
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // No children to collect
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append(".")
    }
}