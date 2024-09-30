package ch.bytecraft.parser.rule.catalog.token

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TokenRuleTest {
    val parser = buildParser("('a' { 'b' { 'c' } 'd' } 'e')[a]")
    val parserEmpty = buildParser("('a' } 'b' 'c' { 'd' 'e')[a]")
    val parserOpen = buildParser("('a' 'b' 'c' 'd' 'e')[a]")

    @Test
    fun `should accept some`() {
        parser.parseChecking("abcde", 0, 5, "a [1,4] 'bcd'")
        parserEmpty.parseChecking("abcde", 0, 5)
        parserOpen.parseChecking("abcde", 0, 5, "a [0,5] 'abcde'")
    }

    @Test
    fun `should reject some`() {
        assertThrows<Throwable> { parser.parse("abc", 0) }
        assertThrows<Throwable> { parser.parse("abce", 0) }
        assertThrows<Throwable> { parser.parse("bcde", 0) }
        assertThrows<Throwable> { parserEmpty.parse("abc", 0) }
        assertThrows<Throwable> { parserEmpty.parse("abce", 0) }
        assertThrows<Throwable> { parserEmpty.parse("bcde", 0) }
        assertThrows<Throwable> { parserOpen.parse("abc", 0) }
        assertThrows<Throwable> { parserOpen.parse("abce", 0) }
        assertThrows<Throwable> { parserOpen.parse("bcde", 0) }
    }
}