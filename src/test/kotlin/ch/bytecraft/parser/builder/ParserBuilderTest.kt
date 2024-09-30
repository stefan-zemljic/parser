package ch.bytecraft.parser.builder

import ch.bytecraft.parser.builder.ParserBuilderException.*
import ch.bytecraft.parser.test_util.CheckingParserBuilder
import ch.bytecraft.parser.test_util.buildParserFail
import ch.bytecraft.parser.test_util.buildParserFailDef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ParserBuilderTest {
    @Test
    fun `build and print parsers`() {
        CheckingParserBuilder()
            .defNoInclude("*test: 'a' () ('abc' | 'def')")
            .def("a: 'a'{3} [^c][q] [b]{2,3} | \${2,} }+ {* ;")
            .build("*a a")
        buildParserFailDef<ExpectedRegexException>(0, "")
            .apply { assertThat(regex.pattern).isEqualTo(ParserBuilderImpl().rName.pattern) }
        buildParserFailDef<ExpectedLiteralException>(1, "a")
        buildParserFailDef<ExpectedLiteralException>(1, "a?")
        buildParserFailDef<DuplicateRuleNameException>(6, "a: 'a'", "a: 'b'")
            .apply { assertThat(name).isEqualTo("a") }
        buildParserFailDef<ExpectedRuleException>(3, "a: ")
        buildParserFail<ExpectedRuleException>(0, "")
        buildParserFail<ExpectedRuleException>(5, "'a' |")
        buildParserFail<ExpectedLiteralException>(1, "(")
        buildParserFail<ExpectedLiteralException>(1, "'")
        buildParserFail<ExpectedRegexException>(4, "'a'[")
    }
}