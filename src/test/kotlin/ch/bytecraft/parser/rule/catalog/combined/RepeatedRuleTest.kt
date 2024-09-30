package ch.bytecraft.parser.rule.catalog.combined

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.rule.catalog.basic.AnyRule
import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RepeatedRuleTest {
    val parserStar = buildParser("'a'*")
    val parserPlus = buildParser("'a'+")
    val parserQuestion = buildParser("'a'?")
    val parserRange = buildParser("'a'{2,4}")
    val parserRangeEmpty = buildParser("'a'{0,3}")
    val parserRangeSingle = buildParser("'a'{2}")
    val parserRangeOpen = buildParser("'a'{2,}")
    val parserRangeStar = Parser.builder().build("'a'{0,}")
        .also { assertThat(it).hasToString("'a'*") }
    val parserEmpty = buildParser("''{1,3}")

    @Test
    fun `should accept some`() {
        parserStar.parseChecking("", 0, 0)
        parserStar.parseChecking("b", 0, 0)
        parserStar.parseChecking("a", 0, 1)
        parserStar.parseChecking("aa", 0, 2)
        parserPlus.parseChecking("a", 0, 1)
        parserPlus.parseChecking("aa", 0, 2)
        parserQuestion.parseChecking("", 0, 0)
        parserQuestion.parseChecking("a", 0, 1)
        parserRange.parseChecking("aa", 0, 2)
        parserRange.parseChecking("aaa", 0, 3)
        parserRange.parseChecking("aaaa", 0, 4)
        parserRange.parseChecking("aaaaa", 0, 4)
        parserRangeEmpty.parseChecking("", 0, 0)
        parserRangeEmpty.parseChecking("a", 0, 1)
        parserRangeEmpty.parseChecking("aa", 0, 2)
        parserRangeEmpty.parseChecking("aaa", 0, 3)
        parserRangeEmpty.parseChecking("aaaa", 0, 3)
        parserRangeSingle.parseChecking("aa", 0, 2)
        parserRangeOpen.parseChecking("aa", 0, 2)
        parserRangeOpen.parseChecking("aaa", 0, 3)
        parserRangeOpen.parseChecking("aaaa", 0, 4)
        parserRangeStar.parseChecking("", 0, 0)
        parserRangeStar.parseChecking("a", 0, 1)
        parserRangeStar.parseChecking("aa", 0, 2)
        parserRangeStar.parseChecking("aaa", 0, 3)
        parserRangeStar.parseChecking("aaaa", 0, 4)
        parserEmpty.parseChecking("abc", 0, 0)
    }

    @Test
    fun `should reject some`() {
        assertThrows<Throwable> { parserPlus.parse("", 0) }
        assertThrows<Throwable> { parserRange.parse("", 0) }
        assertThrows<Throwable> { parserRange.parse("a", 0) }
        assertThrows<Throwable> { parserRange.parse("ababa", 0) }
        assertThrows<Throwable> { parserRangeSingle.parse("a", 0) }
        assertThrows<Throwable> { parserRangeSingle.parse("aba", 0) }
        assertThrows<Throwable> { parserRangeOpen.parse("", 0) }
        assertThrows<Throwable> { parserRangeOpen.parse("a", 0) }
        assertThrows<Throwable> { parserRangeOpen.parse("ababa", 0) }
    }

    @Test
    fun `should check numbers`() {
        assertThrows<Throwable> { RepeatedRule(-1, 0, AnyRule) }
        assertThrows<Throwable> { Parser.builder().build("'a'{3,2}") }
        assertThrows<Throwable> { Parser.builder().build("'a'{1,1}") }
        assertThrows<Throwable> { Parser.builder().build("'a'{0,0}") }
    }
}