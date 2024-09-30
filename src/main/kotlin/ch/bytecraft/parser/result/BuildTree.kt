package ch.bytecraft.parser.result

import ch.bytecraft.linked_stack.LinkedStack
import ch.bytecraft.parser.state.Token

internal fun buildTree(
    source: String,
    tokens: LinkedStack<Token>,
): ArrayList<ParsedNodeOrLeaf> {
    if (tokens.isEmpty()) return arrayListOf()
    val sortedTokens = tokens.peekAll().apply { sort() }
    val result = arrayListOf<ParsedNodeOrLeaf>()
    val children = arrayListOf<ParsedNodeOrLeaf>()
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
            result.add(ParsedLeaf(token.name, start, end, value))
        } else {
            val subTrees = children.reversed()
            children.clear()
            result.add(ParsedNode(token.name, start, end, subTrees))
        }
    }
    return result
}