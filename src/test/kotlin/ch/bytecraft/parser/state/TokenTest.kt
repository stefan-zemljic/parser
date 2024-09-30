package ch.bytecraft.parser.state

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TokenTest {
    @Test
    fun `test toString`() {
        assertThat(Token("a", 0, 1)).hasToString("a [0,1]")
    }
}