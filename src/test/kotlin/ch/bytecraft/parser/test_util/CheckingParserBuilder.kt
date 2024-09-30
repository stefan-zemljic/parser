package ch.bytecraft.parser.test_util

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.builder.ParserBuilder
import org.assertj.core.api.Assertions.assertThat
import kotlin.math.exp

internal class CheckingParserBuilder : ParserBuilder {
    private val builder = ParserBuilder()
    private val expected = StringBuilder()

    fun defNoInclude(source: String): CheckingParserBuilder {
        builder.def(source)
        return this
    }

    override fun def(source: String): CheckingParserBuilder {
        builder.def(source)
        expected.append(source).append('\n')
        return this
    }

    override fun build(source: String): Parser {
        val parser = builder.build(source)
        assertThat(parser).hasToString(expected.toString() + source)
        return parser
    }
}