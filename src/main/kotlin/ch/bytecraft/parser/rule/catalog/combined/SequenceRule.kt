package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.cheks.chek
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringOrSequenceRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringRule
import ch.bytecraft.parser.state.State

internal class SequenceRule(val rules: List<ParseRule>) : ChoiceOrRecoveringOrSequenceRule() {
    init {
        chek { rules.count() != 1 }
    }

    override fun parse(state: State): Boolean {
        if (rules.isEmpty()) return true
        val it = rules.iterator()
        var doThrow = false
        try {
            if (!it.next().parse(state)) return false
            while (it.hasNext()) if (!it.next().parse(state)) {
                doThrow = true
                break
            }
        } catch (t: Throwable) {
            throw RuntimeException("Error parsing $this", t)
        }
        if (doThrow) throw RuntimeException("Expected $this")
        return true
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        rules.forEach(block)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        if (rules.isEmpty()) {
            if (!isWrapped) builder.append("()")
        } else {
            rules.first().wrappedAppendTo<ChoiceOrRecoveringRule>(builder)
            rules.drop(1).forEach {
                builder.append(" ")
                it.wrappedAppendTo<ChoiceOrRecoveringRule>(builder)
            }
        }
    }
}