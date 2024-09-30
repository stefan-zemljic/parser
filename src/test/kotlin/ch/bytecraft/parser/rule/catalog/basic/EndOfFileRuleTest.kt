package ch.bytecraft.parser.rule.catalog.basic

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EndOfFileRuleTest {
    private val parser = buildParser("$")

    @Test
    fun `should accept end of file`() {
        parser.parseChecking("", 0, 0)
        parser.parseChecking("a", 1, 1)
        parser.parseChecking("ab", 2, 2)
        parser.parseChecking("abc", 3, 3)
    }

    @Test
    fun `should reject otherwise`() {
        assertThrows<Throwable> { parser.parse("abc", 0) }
        assertThrows<Throwable> { parser.parse("abc", 1) }
        assertThrows<Throwable> { parser.parse("abc", 2) }
    }
}