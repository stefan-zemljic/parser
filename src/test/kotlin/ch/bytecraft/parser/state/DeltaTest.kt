package ch.bytecraft.parser.state

import ch.bytecraft.parser.test_util.buildParser
import ch.bytecraft.parser.test_util.parseChecking
import org.junit.jupiter.api.Test

class DeltaTest {
    @Test
    fun `applyTo respects start and end changes`() {
        val parser = buildParser(
            "*a: 'a' { 'b' } 'c'",
            "(a 'a' ; | a)[rule]",
        )
        parser.parseChecking("abc", 0, 3, "rule [1,2] 'b'")
    }
}