package ch.bytecraft.parser.rule.catalog.token

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringOrSequenceRule
import ch.bytecraft.parser.state.State
import ch.bytecraft.parser.state.Token

internal class TokenRule(val name: String, val rule: ParseRule) : ParseRule() {
    override fun parse(state: State): Boolean {
        val start = state.index
        state.startsOfToken.push(Int.MAX_VALUE)
        state.endsOfToken.push(Int.MIN_VALUE)
        try {
            if (!rule.parse(state)) return false
            var actualStart = state.startsOfToken.peek()
            if (actualStart == Int.MAX_VALUE) actualStart = start
            var actualEnd = state.endsOfToken.peek()
            if (actualEnd == Int.MIN_VALUE) actualEnd = state.index
            if (actualStart <= actualEnd)
                state.tokens.push(Token(name, actualStart, actualEnd))
            return true
        } finally {
            state.startsOfToken.pop()
            state.endsOfToken.pop()
        }
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        block(rule)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        rule.wrappedAppendTo<ChoiceOrRecoveringOrSequenceRule>(builder)
        builder.append('[', name, ']')
    }
}