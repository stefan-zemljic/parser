package ch.bytecraft.parser.rule

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.builder.ParserBuilderException.UnknownRulesException
import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.buildParserFail
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParseRuleTest {
    @Test
    fun `resolveNamedRules works given known rules`() {
        buildParser(
            "rule: 'a'",
            "other: rule",
            "other",
        )
    }

    @Test
    fun `resolveNamedRules fails on missing rule`() {
        buildParserFail<UnknownRulesException>(4, "rule")
            .apply { assertThat(names).containsExactly("rule") }
    }

    @Test
    fun `toString with a TokenRule with name mismatch`() {
        buildParser(
            "*rule: 'a'[name]",
            "rule"
        )
    }

    @Test
    fun `toString is ordered as in the source`() {
        val string = Parser.builder()
            .def("apple: mango")
            .def("tree: apple")
            .def("mango: melon")
            .def("melon: 'a'")
            .build("tree")
            .toString()
        val expected = "" +
                "apple: mango\n" +
                "tree: apple\n" +
                "mango: melon\n" +
                "melon: 'a'\n" +
                "tree"
        assertThat(string).isEqualTo(expected)
    }
}