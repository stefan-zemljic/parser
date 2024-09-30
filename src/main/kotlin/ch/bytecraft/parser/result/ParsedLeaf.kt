package ch.bytecraft.parser.result

class ParsedLeaf(
    override val name: String,
    start: Int,
    end: Int,
    val value: String,
) : ParsedNodeOrLeaf(
    name = name,
    start = start,
    end = end,
    source = value,
)
