package ch.bytecraft.parser.result

class ParsedNode(
    override val name: String,
    start: Int,
    end: Int,
    override val children: List<ParsedNodeOrLeaf>,
) : ParsedNodeOrLeaf(
    name = name,
    start = start,
    end = end,
    children = children,
)