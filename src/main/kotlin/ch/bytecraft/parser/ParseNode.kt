package ch.bytecraft.parser

import ch.bytecraft.escaper.Escaper
import ch.bytecraft.linked_stack.LinkedStack
import ch.bytecraft.parser.state.Token

sealed class ParseNode {
    companion object {
        internal fun buildTree(
            source: String,
            tokens: LinkedStack<Token>,
        ): ArrayList<ParseNode> {
            if (tokens.isEmpty()) return arrayListOf()
            val sortedTokens = tokens.peekAll().apply { sort() }
            val result = arrayListOf<ParseNode>()
            val children = arrayListOf<ParseNode>()
            for (token in sortedTokens) {
                val start = token.start
                val end = token.end
                while (true) {
                    val last = result.lastOrNull() ?: break
                    if (last.start < start) break
                    children.add(last)
                    result.removeLast()
                }
                if (children.isEmpty()) {
                    val value = source.substring(start, end)
                    result.add(Leaf(token.name, start, end, value))
                } else {
                    val subTrees = children.reversed()
                    children.clear()
                    result.add(Parent(token.name, start, end, subTrees))
                }
            }
            return result
        }
    }

    abstract val name: String
    abstract val start: Int
    abstract val end: Int

    internal abstract fun appendTo(builder: StringBuilder, indent: Int)

    data class Parent(
        override val name: String,
        override val start: Int,
        override val end: Int,
        val children: List<ParseNode>,
    ) : ParseNode() {
        override fun appendTo(builder: StringBuilder, indent: Int) {
            repeat(indent) { builder.append("  ") }
            builder.append(name)
            builder.append(" [", start, ",", end, "]")
            val newIndent = indent + 1
            children.forEach {
                builder.append('\n')
                it.appendTo(builder, newIndent)
            }
        }

        override fun toString(): String {
            return buildString { appendTo(this, 0) }
        }
    }

    data class Leaf(
        override val name: String,
        override val start: Int,
        override val end: Int,
        val value: String,
    ) : ParseNode() {
        companion object {
            private val escaper = Escaper(
                escape = '\\'.code,
                replacements = mapOf(
                    '\\'.code to '\\'.code,
                    't'.code to '\t'.code,
                    'r'.code to '\r'.code,
                    'n'.code to '\n'.code,
                ),
                handleRanges = false,
            )
        }

        override fun appendTo(builder: StringBuilder, indent: Int) {
            repeat(indent) { builder.append("  ") }
            builder.append(name)
            builder.append(" [", start, ',', end, "] '")
            escaper.escapeTo(builder, value)
            builder.append("'")
        }

        override fun toString(): String {
            return buildString { appendTo(this, 0) }
        }
    }
}