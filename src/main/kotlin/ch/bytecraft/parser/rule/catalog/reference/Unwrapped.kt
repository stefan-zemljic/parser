package ch.bytecraft.parser.rule.catalog.reference

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal class Unwrapped(val rule: NamedRule) : ParseRule() {
    override fun parse(state: State): Boolean {
        return rule.parse(state, true)
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        block(rule)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append('*')
        rule.appendTo(builder, isWrapped)
    }
}