package ch.bytecraft.parser.result

class ParsedRoot(
    start: Int,
    end: Int,
    override val children: List<ParsedNodeOrLeaf>,
) : ParsedResult(
    start = start,
    end = end,
    children = children,
)