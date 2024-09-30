package ch.bytecraft.parser.rule.catalog.reference

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.catalog.token.TokenRule
import ch.bytecraft.parser.state.Delta
import ch.bytecraft.parser.state.State

internal class NamedRule(val name: String) : ParseRule() {
    var lineIndex: Int = 0
    lateinit var rule: ParseRule

    override fun parse(state: State): Boolean {
        return parse(state, false)
    }

    fun parse(state: State, unwrap: Boolean): Boolean {
        val deltas = state.deltaByIndexAndRuleName
        val key = state.index to name
        if (deltas.containsKey(key)) {
            val delta = deltas[key] ?: return false
            delta.applyTo(state)
            return true
        }
        val tokensBefore = state.tokens.head
        val success = rule.let { if (unwrap && it is TokenRule) it.rule else it }.parse(state)
        val delta = if (success) Delta(tokensBefore, state) else null
        state.deltaByIndexAndRuleName[key] = delta
        return success
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        if (::rule.isInitialized) block(rule)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append(name)
    }
}
