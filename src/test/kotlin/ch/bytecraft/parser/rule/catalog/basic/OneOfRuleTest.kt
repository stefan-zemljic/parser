package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.test_util.parseChecking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OneOfRuleTest {
    val emoji = "👍".also { assertThat(it).isEqualTo("\uD83D\uDC4D") }

    private val parser = Parser(OneOfRule(false, "abc"))
    private val complementParser = Parser(OneOfRule(true, "a-c"))
    private val emptyParser = Parser(OneOfRule(false, ""))
    private val emojiParser = Parser(OneOfRule(false, emoji))
    private val halfUnicodeParser = Parser(OneOfRule(false, "a\uD83Db"))
    private val escapesParser = Parser(OneOfRule(false, """\t \r \n \\ \^ \] \-"""))
    private val escapesParser2 = Parser(OneOfRule(false, """\t \r \n \ ^ ] -"""))

    @Test
    fun `should accept one of the characters`() {
        parser.parseChecking("ab", 0, 1)
        parser.parseChecking("ab", 1, 2)
        parser.parseChecking("cd", 0, 1)
        complementParser.parseChecking("d", 0, 1)
        complementParser.parseChecking("e", 0, 1)
        emojiParser.parseChecking(emoji, 0, 2)
        emojiParser.parseChecking("a${emoji}b", 1, 3)
        halfUnicodeParser.parseChecking("b", 0, 1)
        halfUnicodeParser.parseChecking("\uD83Db", 0, 1)
    }

    @Test
    fun `should reject non-matching characters`() {
        assertThrows<Throwable> { parser.parse("de", 0) }
        assertThrows<Throwable> { parser.parse("e", 0) }
        assertThrows<Throwable> { complementParser.parse("a", 0) }
        assertThrows<Throwable> { complementParser.parse("b", 0) }
        assertThrows<Throwable> { complementParser.parse("c", 0) }
        assertThrows<Throwable> { emojiParser.parse("a", 0) }
        assertThrows<Throwable> { emojiParser.parse("\uD83Db", 0) }
        assertThrows<Throwable> { emptyParser.parse("abc", 0) }
    }

    @Test
    fun `should reject end of input`() {
        assertThrows<Throwable> { parser.parse("ab", 2) }
        assertThrows<Throwable> { complementParser.parse("", 0) }
        assertThrows<Throwable> { emojiParser.parse("", 0) }
        assertThrows<Throwable> { emptyParser.parse("", 0) }
    }

    @Test
    fun `should print as expected`() {
        assertThat(parser).hasToString("[a-c]")
        assertThat(complementParser).hasToString("[^a-c]")
        assertThat(emptyParser).hasToString("[]")
        assertThat(emojiParser).hasToString("[\\u{1f44d}]")
        assertThat(halfUnicodeParser).hasToString("[a\uD83Db]")
        assertThat(escapesParser2).hasToString("[\\t \\r\\n\\\\\\^\\]\\-]")
        assertThat(escapesParser).hasToString("[\\t \\r\\n\\\\\\^\\]\\-]")
    }
}