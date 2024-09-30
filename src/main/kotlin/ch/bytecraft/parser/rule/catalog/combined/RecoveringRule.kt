package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.unions.ChoiceOrRecoveringRule
import ch.bytecraft.parser.state.Fallback
import ch.bytecraft.parser.state.State

internal class RecoveringRule(
    val prefix: ParseRule?,
    val suffix: ParseRule?,
) : ChoiceOrRecoveringRule() {
    override fun parse(state: State): Boolean {
        val fallback = if (prefix != null) {
            val fallback = Fallback(state)
            val result = try {
                prefix.parse(state)
            } catch (t: Throwable) {
                fallback.applyTo(state)
                return false
            }
            if (!result) return false
            fallback
        } else null
        if (suffix == null) return true
        try {
            if (suffix.parse(state)) return true
        } catch (t: Throwable) {
            throw RuntimeException("Error parsing $this", t)
        }
        fallback?.applyTo(state)
        return false
    }

    override fun collectChildren(block: (ParseRule) -> Unit) {
        prefix?.let(block)
        suffix?.let(block)
    }

    override fun appendTo(builder: StringBuilder, isWrapped: Boolean) {
        if (prefix != null) {
            prefix.wrappedAppendTo<ChoiceRule>(builder)
            builder.append(' ')
        }
        builder.append(";")
        if (suffix != null) {
            builder.append(' ')
            suffix.wrappedAppendTo<ChoiceRule>(builder)
        }
    }
}