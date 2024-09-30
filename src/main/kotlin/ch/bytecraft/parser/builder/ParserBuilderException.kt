package ch.bytecraft.parser.builder

import ch.bytecraft.escaper.Escaper

sealed class ParserBuilderException(
    override val message: String,
    override val cause: ParserBuilderException? = null,
) : RuntimeException(message, cause) {
    companion object {
        internal val escaper = Escaper(
            escape = '\\'.code,
            replacements = mapOf(
                '\t'.code to 't'.code,
                '\r'.code to 'r'.code,
                '\n'.code to 'n'.code,
                '\''.code to '\''.code,
            ),
            handleRanges = false,
        )
    }

    class ExpectedLiteralException(
        val expected: String,
    ) : ParserBuilderException(
        "Expected literal '${escaper.escape(expected)}'"
    )

    class ExpectedRegexException(
        val regex: Regex,
    ) : ParserBuilderException(
        "Expected regex '${escaper.escape(regex.pattern)}'"
    )

    class ExpectedRuleException :
        ParserBuilderException("Expected rule")

    class DuplicateRuleNameException(
        val name: String,
    ) : ParserBuilderException(
        "Duplicate rule name '$name'"
    )

    class UnknownRulesException(
        val names: Set<String>,
    ) : ParserBuilderException(
        "Unknown rules: ${names.joinToString(", ")}"
    )

    class WrappedException(
        val source: String,
        val index: Int,
        override val cause: ParserBuilderException,
    ) : ParserBuilderException(
        "${cause.message} at $index '${
            escaper.escape(source.substring(0, index))
        }[=>]${
            escaper.escape(source.substring(index))
        }'", cause
    )
}