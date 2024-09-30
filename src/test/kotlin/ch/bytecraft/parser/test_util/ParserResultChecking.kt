package ch.bytecraft.parser.test_util

import ch.bytecraft.parser.Parser
import org.assertj.core.api.Assertions.assertThat

fun Parser.parseChecking(input: String, start: Int, end: Int, vararg nodes: String) {
    parse(input, start).also {
        assertThat(it.start).isEqualTo(start)
        assertThat(it.end).isEqualTo(end)
        if (nodes.isEmpty()) {
            assertThat(it).hasToString("[$start,$end]")
        } else {
            assertThat(it).hasToString("[$start,$end]\n" + nodes.joinToString("\n").prependIndent("  "))
        }
    }
}
