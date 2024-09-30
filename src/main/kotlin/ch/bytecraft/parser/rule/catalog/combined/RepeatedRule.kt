package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.cheks.chek
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringOrSequenceRule
import ch.bytecraft.parser.state.State

internal class RepeatedRule(
    val min: Int,
    val max: Int,
    val rule: ParseRule,
) : ParseRule() {
    init {
        chek { min >= 0 }
        chek { min <= max }
        chek { min == 0 && max == 1 || max > 1 }
    }

    override fun parse(state: State): Boolean {
        val start = state.index
        var count = 0
        while (count < max) {
            if (!rule.parse(state)) break
            if (state.index == start) {
                count = min
                break
            }
            count++
        }
        if (count >= min) return true
        if (count != 0) error("Expected at least $min repetitions of $rule but got $count")
        return false
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        block(rule)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        rule.wrappedAppendTo<ChoiceOrRecoveringOrSequenceRule>(builder)
        when (min) {
            0 -> when (max) {
                1 -> builder.append('?')
                Int.MAX_VALUE -> builder.append('*')
                else -> builder.append("{0,", max, '}')
            }
            1 -> when (max) {
                Int.MAX_VALUE -> builder.append('+')
                else -> builder.append("{1,", max, '}')
            }
            else -> when (max) {
                min -> builder.append('{', min, '}')
                Int.MAX_VALUE -> builder.append('{', min, ",}")
                else -> builder.append('{', min, ',', max, '}')
            }
        }
    }
}