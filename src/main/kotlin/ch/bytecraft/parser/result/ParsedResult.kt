package ch.bytecraft.parser.result

import ch.bytecraft.escaper.Escaper

sealed class ParsedResult(
    open val name: String? = null,
    val start: Int,
    val end: Int,
    open val content: String? = null,
    open val children: List<ParsedNodeOrLeaf>? = null,
) {
    companion object {
        private val escaper = Escaper(
            escape = '\\'.code,
            replacements = mapOf(
                '\t'.code to 't'.code,
                '\r'.code to 'r'.code,
                '\n'.code to 'n'.code,
                '\\'.code to '\\'.code,
            ),
            handleRanges = false,
        )
    }

    fun appendTo(builder: StringBuilder, indent: Int) {
        name?.let { builder.append(it, ' ') }
        builder.append("[$start,$end]")
        content?.let {
            builder.append(" '")
            escaper.escapeTo(builder, it)
            builder.append('\'')
        }
        val newIndent = indent + 1
        children?.forEach {
            builder.append("\n")
            repeat(newIndent) { builder.append("  ") }
            it.appendTo(builder, newIndent)
        }
    }

    override fun toString(): String {
        return buildString { appendTo(this, 0) }
    }
}