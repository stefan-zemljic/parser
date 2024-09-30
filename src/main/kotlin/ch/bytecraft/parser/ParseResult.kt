package ch.bytecraft.parser

class ParseResult(
    val start: Int,
    val end: Int,
    val nodes: List<ParseNode>,
)