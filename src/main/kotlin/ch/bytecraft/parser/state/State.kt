package ch.bytecraft.parser.state

import ch.bytecraft.linked_stack.LinkedStack

internal class State(val source: String, startIndex: Int) {
    val deltaByIndexAndRuleName = mutableMapOf<Pair<Int, String>, Delta?>()
    val tokens = LinkedStack<Token>()
    val startsOfToken = LinkedStack<Int>()
    val endsOfToken = LinkedStack<Int>()
    var index = startIndex
}

