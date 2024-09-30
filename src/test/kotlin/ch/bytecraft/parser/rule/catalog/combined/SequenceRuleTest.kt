package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.parser.rule.catalog.basic.AnyRule
import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class SequenceRuleTest {
    val parser = buildParser("'a' 'b' 'c'")
    val emptyParser = buildParser("()")
    val emptyTokenParser = buildParser("()[a]")
    val subSequenceParser = buildParser("'a' ('b' | 'c' 'd')")

    @Test
    fun `should accept the sequence`() {
        parser.parseChecking("abc", 0, 3)
        parser.parseChecking("aabc", 1, 4)
        emptyParser.parseChecking("", 0, 0)
        emptyParser.parseChecking("a", 1, 1)
        emptyTokenParser.parseChecking("a", 0, 0, "a [0,0] ''")
        subSequenceParser.parseChecking("abd", 0, 2)
        subSequenceParser.parseChecking("acd", 0, 3)
    }

    @Test
    fun `should reject others`() {
        assertThrows<Throwable> { parser.parse("ab", 0) }
        assertThrows<Throwable> { parser.parse("abdcd", 0) }
        assertThrows<Throwable> { parser.parse("abdcd", 1) }
        assertThrows<Throwable> { parser.parse("abdcd", 2) }
        assertThrows<Throwable> { parser.parse("aac", 0) }
        assertThrows<Throwable> { parser.parse("ab", 3) }
        assertThrows<Throwable> { subSequenceParser.parse("ac", 0) }
    }

    @Test
    fun `should require 0 or more than 1 rules`() {
        assertThrows<Throwable> { SequenceRule(listOf(AnyRule)) }
    }
}