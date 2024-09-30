package ch.bytecraft.parser

import ch.bytecraft.linked_stack.LinkedStack
import ch.bytecraft.parser.result.ParsedResult
import ch.bytecraft.parser.result.buildTree
import ch.bytecraft.parser.state.Token
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ParsedNodeTest {
    @Test
    fun `should create correct nesting`() {
        val tokens = LinkedStack<Token>().apply {
            push(Token("a", 0, 1))
            push(Token("c", 2, 3))
            push(Token("d", 0, 2))
            push(Token("b", 1, 2))
            push(Token("b", 1, 2))
            push(Token("b", 1, 2))
        }
        assertThat(buildTree("abc", tokens).joinToString("\n")).isEqualTo(
            listOf(
                "d [0,2]",
                "  a [0,1] 'a'",
                "  b [1,2]",
                "    b [1,2]",
                "      b [1,2] 'b'",
                "c [2,3] 'c'",
            ).joinToString("\n")
        )
    }

    @Test
    fun `should return emptyList if no tokens`() {
        assertThat(buildTree("", LinkedStack()).isEmpty()).isTrue()
    }
}