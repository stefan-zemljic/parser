package ch.bytecraft

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AssertionsTest {
    @Test
    fun `should fail`() {
        assertThrows<AssertionError> { assert { false } }
            .message.let { assertThat(it).isEqualTo("Assertion failed") }
        assertThrows<AssertionError> { assert({ false }) { "Custom message" } }
            .message.let { assertThat(it).isEqualTo("Custom message") }
    }

    @Test
    fun `should pass`() {
        assert { true }
        assert({ true }) { "Custom message" }
    }

    @Test
    fun `should not execute if disabled`() {
        Assertions.enabled = false
        try {
            var executed = false
            assert { executed = true; true }
            assertThat(executed).isFalse()
            assert({ executed = true; true }) { "Custom message" }
            assertThat(executed).isFalse()
        } finally {
            Assertions.enabled = true
        }
    }
}