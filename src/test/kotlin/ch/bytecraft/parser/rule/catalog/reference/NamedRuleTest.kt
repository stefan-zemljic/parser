package ch.bytecraft.parser.rule.catalog.reference

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class NamedRuleTest {
    val parser = buildParser("*rule: 'a'", "rule")
    val tokenParser = buildParser("rule: 'a'", "rule")
    val parserUnwrap = buildParser("rule: 'a'", "*rule")
    val parserUnwrapUseless = buildParser("*rule: 'a'", "*rule")
    val parserCached = buildParser("rule: 'a'", "rule 'a' ; | rule")

    @Test
    fun `should accept some`() {
        parser.parseChecking("a", 0, 1)
        tokenParser.parseChecking("a", 0, 1, "rule [0,1] 'a'")
        parserUnwrap.parseChecking("a", 0, 1)
        parserUnwrapUseless.parseChecking("a", 0, 1)
        parserCached.parseChecking("a", 0, 1, "rule [0,1] 'a'")
    }

    @Test
    fun `should reject some`() {
        assertThrows<Throwable> { parser.parse("b", 0) }
        assertThrows<Throwable> { parser.parse("aba", 1) }
        assertThrows<Throwable> { parserCached.parse("b", 0) }
    }
}