package ch.bytecraft.parser.result

sealed class ParsedNodeOrLeaf(
    name: String,
    start: Int,
    end: Int,
    source: String? = null,
    children: List<ParsedNodeOrLeaf>? = null,
) : ParsedResult(
    name = name,
    start = start,
    end = end,
    content = source,
    children = children,
)