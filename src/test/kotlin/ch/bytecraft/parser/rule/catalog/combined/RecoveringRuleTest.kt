package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RecoveringRuleTest {
    val parser = buildParser("('a'? 'b' ; 'c' 'd')?")
    val parserNoSuffix = buildParser("('a' ;)?")
    val parserEmptySuffix = buildParser("('a' ; ())?")
    val parserNoPrefix = buildParser("(; 'b' 'c')?")
    val parserEmptyPrefix = buildParser("(() ; 'b' 'c')?")

    @Test
    fun `should accept some`() {
        parser.parseChecking("", 0, 0)
        parser.parseChecking("b", 0, 0)
        parser.parseChecking("a", 0, 0)
        parser.parseChecking("bcd", 0, 3)
        parser.parseChecking("abcd", 0, 4)
        parserNoSuffix.parseChecking("a", 0, 1)
        parserNoSuffix.parseChecking("b", 0, 0)
        parserEmptySuffix.parseChecking("a", 0, 1)
        parserEmptySuffix.parseChecking("b", 0, 0)
        parserNoPrefix.parseChecking("a", 0, 0)
        parserNoPrefix.parseChecking("bc", 0, 2)
        parserEmptyPrefix.parseChecking("a", 0, 0)
        parserEmptyPrefix.parseChecking("bc", 0, 2)
    }

    @Test
    fun `should reject some`() {
        assertThrows<Throwable> { parser.parse("bc", 0) }
        assertThrows<Throwable> { parser.parse("abc", 0) }
        assertThrows<Throwable> { parser.parse("bc", 0) }
        assertThrows<Throwable> { parser.parse("abcc", 0) }
        assertThrows<Throwable> { parserNoPrefix.parse("ba", 0) }
        assertThrows<Throwable> { parserEmptyPrefix.parse("ba", 0) }
    }
}