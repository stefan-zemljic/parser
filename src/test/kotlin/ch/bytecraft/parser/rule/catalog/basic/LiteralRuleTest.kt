package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.test_util.parseChecking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LiteralRuleTest {
    val emoji = "\uD83D\uDE0A".also { assertThat(it).isEqualTo("😊") }

    val abcParser = Parser(LiteralRule("abc"))
    val emptyParser = Parser(LiteralRule(""))
    val whitespaceParser = Parser(LiteralRule("a\tb\rc\nd'e"))
    val emojiParserEscaped = Parser(LiteralRule("\\u{1F60A}"))
    val emojiParserRaw = Parser(LiteralRule("a$emoji"))

    @Test
    fun `should accept literal`() {
        abcParser.parseChecking("abc", 0, 3)
        abcParser.parseChecking("_abc", 1, 4)
        emojiParserEscaped.parseChecking("a$emoji", 1, 3)
        emojiParserRaw.parseChecking("a${emoji}b", 0, 3)
    }

    @Test
    fun `should reject mismatches`() {
        assertThrows<Throwable> { abcParser.parse("", 0) }
        assertThrows<Throwable> { abcParser.parse("a", 0) }
        assertThrows<Throwable> { abcParser.parse("ab", 0) }
        assertThrows<Throwable> { abcParser.parse("bbc", 0) }
        assertThrows<Throwable> { abcParser.parse("acc", 0) }
        assertThrows<Throwable> { abcParser.parse("abd", 0) }
        assertThrows<Throwable> { abcParser.parse("abcd", 1) }
    }

    @Test
    fun `should accept empty literal`() {
        assertThat(emptyParser.parse("", 0).end).isEqualTo(0)
        assertThat(emptyParser.parse("abc", 0).end).isEqualTo(0)
    }

    @Test
    fun `should print as expected`() {
        assertThat(abcParser).hasToString("'abc'")
        assertThat(emptyParser).hasToString("''")
        assertThat(whitespaceParser).hasToString("'a\\tb\\rc\\nd\\'e'")
        assertThat(emojiParserEscaped).hasToString("'\\u{1f60a}'")
        assertThat(emojiParserRaw).hasToString("'a\\u{1f60a}'")
    }
}