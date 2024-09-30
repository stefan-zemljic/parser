package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.escaper.Escaper
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal class LiteralRule private constructor(val literal: String) : ParseRule() {
    companion object {
        val escaper = Escaper(
            escape = '\\'.code,
            replacements = mapOf(
                '\t'.code to 't'.code,
                '\r'.code to 'r'.code,
                '\n'.code to 'n'.code,
                '\\'.code to '\\'.code,
                '\''.code to '\''.code,
            ),
            handleRanges = false
        )

        operator fun invoke(escaped: String): LiteralRule {
            return LiteralRule(escaper.unescape(escaped))
        }
    }

    override fun parse(state: State): Boolean {
        if (!state.source.startsWith(literal, state.index)) return false
        if (literal.isEmpty()) return true
        state.index += literal.length
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // no children
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append('\'')
        escaper.escapeTo(builder, literal)
        builder.append('\'')
    }
}