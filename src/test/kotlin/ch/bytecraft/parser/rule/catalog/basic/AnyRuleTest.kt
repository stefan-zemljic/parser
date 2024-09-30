package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AnyRuleTest {
    private val parser = buildParser(".")

    @Test
    fun `should accept any character`() {
        parser.parseChecking("abc", 0, 1)
        parser.parseChecking("abc", 1, 2)
        parser.parseChecking("abc", 2, 3)
    }

    @Test
    fun `should reject at end`() {
        assertThrows<Throwable> { parser.parse("", 0) }
        assertThrows<Throwable> { parser.parse("a", 1) }
        assertThrows<Throwable> { parser.parse("ab", 2) }
        assertThrows<Throwable> { parser.parse("abc", 3) }
    }

    @Test
    fun `should accept whole codepoints`() {
        assertThat(parser.parse("\uD83D\uDE0A").end).isEqualTo(2)
    }
}