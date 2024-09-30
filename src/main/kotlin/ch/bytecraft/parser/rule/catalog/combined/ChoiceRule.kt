package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.cheks.chek
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringRule
import ch.bytecraft.parser.state.State

internal class ChoiceRule(val rules: List<ParseRule>) : ChoiceOrRecoveringRule() {
    init {
        chek { rules.count() > 1 }
    }

    override fun parse(state: State): Boolean {
        try {
            for (rule in rules) rule.parse(state) && return true
            return false
        } catch (t: Throwable) {
            throw RuntimeException("Error parsing $this", t)
        }
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        rules.forEach(block)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        var first = true
        for (rule in rules) {
            when {
                first -> first = false
                else -> builder.append(" | ")
            }
            rule.appendTo(builder, true)
        }
    }
}