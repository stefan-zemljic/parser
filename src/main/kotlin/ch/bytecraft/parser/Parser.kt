package ch.bytecraft.parser

import ch.bytecraft.parser.builder.ParserBuilder
import ch.bytecraft.parser.result.ParsedResult
import ch.bytecraft.parser.result.ParsedRoot
import ch.bytecraft.parser.result.buildTree
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.catalog.token.TokenRule
import ch.bytecraft.parser.state.State

class Parser internal constructor(
    private val rule: ParseRule,
) {
    companion object {
        fun builder(): ParserBuilder {
            return ParserBuilder()
        }
    }

    fun parse(source: String, start: Int = 0): ParsedRoot {
        val state = State(source, start)
        try {
            if (!rule.parse(state)) error("Expected $rule")
        } catch (e: Exception) {
            val tokens = state.tokens.peekAll().apply { reverse() }.joinToString("\n  ")
            throw IllegalStateException("Error at index ${state.index}\n  $tokens", e)
        }
        return ParsedRoot(start, state.index, buildTree(source, state.tokens))
    }

    override fun toString(): String {
        val builder = StringBuilder()
        val namedRules = rule.collectNamedRules().toMutableList().apply { sortBy { it.lineIndex } }
        val printedNamedRules = mutableSetOf<String>()
        namedRules.filter { printedNamedRules.add(it.name) }.forEach {
            val subRule = it.rule
            if (subRule is TokenRule && subRule.name == it.name) {
                builder.append(it.name, ": ")
                subRule.rule.appendTo(builder)
            } else {
                builder.append('*', it.name, ": ")
                subRule.appendTo(builder)
            }
            builder.append('\n')
        }
        rule.appendTo(builder)
        return builder.toString()
    }
}

