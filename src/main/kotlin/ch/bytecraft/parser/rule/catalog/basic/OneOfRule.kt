package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.escaper.Escaper
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.state.State

internal class OneOfRule private constructor(val complement: Boolean, val codePoints: Set<Int>) : ParseRule() {
    companion object {
        val escaper = Escaper(
            escape = '\\'.code,
            replacements = mapOf(
                '\t'.code to 't'.code,
                '\r'.code to 'r'.code,
                '\n'.code to 'n'.code,
                '\\'.code to '\\'.code,
                '^'.code to '^'.code,
                '-'.code to '-'.code,
                ']'.code to ']'.code,
            ),
            handleRanges = true,
        )

        operator fun invoke(complement: Boolean, escaped: String): OneOfRule {
            val codePoints = mutableSetOf<Int>()
            escaper.unescape(escaped)
                .codePoints()
                .forEach(codePoints::add)
            return OneOfRule(complement, codePoints)
        }
    }

    override fun parse(state: State): Boolean {
        if (state.index == state.source.length) return false
        val codePoint = state.source.codePointAt(state.index)
        if (codePoint in codePoints == complement) return false
        state.index += Character.charCount(codePoint)
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        // no children
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        builder.append('[')
        if (complement) builder.append('^')
        val length = builder.length
        codePoints.forEach { builder.appendCodePoint(it) }
        val sCodePoints = builder.substring(length)
        builder.setLength(length)
        escaper.escapeTo(builder, sCodePoints)
        builder.append(']')
    }
}