package ch.bytecraft.parser.state

internal data class Token(val name: String, val start: Int, val end: Int) : Comparable<Token> {
    override fun compareTo(other: Token): Int {
        val endCompare = end.compareTo(other.end)
        if (endCompare != 0) return endCompare
        return -start.compareTo(other.start)
    }

    override fun toString(): String {
        return "$name [$start,$end]"
    }
}