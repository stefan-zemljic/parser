package ch.bytecraft.parser.test_util

import ch.bytecraft.parser.Parser
import org.assertj.core.api.Assertions.assertThat

fun Parser.parseChecking(input: String, start: Int, end: Int, vararg nodes: String = arrayOf("")) {
    parse(input, start).also {
        assertThat(it.start).isEqualTo(start)
        assertThat(it.end).isEqualTo(end)
        assertThat(it.nodes.joinToString("\n")).isEqualTo(nodes.joinToString("\n"))
    }
}
