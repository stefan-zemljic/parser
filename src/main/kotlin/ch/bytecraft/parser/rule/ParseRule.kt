package ch.bytecraft.parser.rule

import ch.bytecraft.parser.rule.catalog.reference.NamedRule
import ch.bytecraft.parser.state.State

internal abstract class ParseRule {
    abstract fun parse(state: State): Boolean
    abstract fun collectChildren(block: (ParseRule) -> Unit)
    abstract fun appendTo(builder: StringBuilder, isWrapped: Boolean = false)

    fun collectNamedRules(notFound: MutableSet<String>? = null): Set<NamedRule> {
        val namedRules = mutableSetOf<NamedRule>()
        val todo = mutableListOf(this)
        while (todo.isNotEmpty()) {
            val rule = todo.removeLast()
            if (rule is NamedRule) {
                if (!namedRules.add(rule)) continue
                if (notFound != null) {
                    runCatching { rule.rule }
                        .onFailure {
                            println("Rule ${rule.name} not found")
                            notFound.add(rule.name)
                        }
                }
            }
            rule.collectChildren(todo::add)
        }
        return namedRules
    }

    inline fun <reified T : Any> wrappedAppendTo(builder: StringBuilder) {
        if (this is T) {
            builder.append('(')
            appendTo(builder, isWrapped = true)
            builder.append(')')
            return
        } else {
            appendTo(builder, isWrapped = false)
        }
    }

    override fun toString(): String {
        return buildString { appendTo(this) }
    }
}