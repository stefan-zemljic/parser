package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.parser.rule.catalog.basic.AnyRule
import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ChoiceRuleTest {
    val parser = buildParser("'a' | 'b' | 'c'")

    @Test
    fun `should accept all options`() {
        parser.parseChecking("a", 0, 1)
        parser.parseChecking("ab", 1, 2)
        parser.parseChecking("c", 0, 1)
    }

    @Test
    fun `should reject others`() {
        assertThrows<Throwable> { parser.parse("d", 0) }
        assertThrows<Throwable> { parser.parse("ad", 1) }
    }

    @Test
    fun `should assert at least two options`() {
        assertThrows<Throwable> { ChoiceRule(listOf()) }
        assertThrows<Throwable> { ChoiceRule(listOf(AnyRule)) }
    }
}