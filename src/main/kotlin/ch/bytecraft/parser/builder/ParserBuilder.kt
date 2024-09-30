package ch.bytecraft.parser.builder

import ch.bytecraft.parser.Parser

interface ParserBuilder {
    companion object {
        internal operator fun invoke(): ParserBuilder = ParserBuilderImpl()
    }

    fun def(source: String): ParserBuilder
    fun build(source: String): Parser
}

