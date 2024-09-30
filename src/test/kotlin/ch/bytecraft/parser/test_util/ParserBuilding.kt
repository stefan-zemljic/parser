package ch.bytecraft.parser.test_util

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.builder.ParserBuilder
import ch.bytecraft.parser.builder.ParserBuilderException
import ch.bytecraft.parser.builder.ParserBuilderException.WrappedException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows

internal fun buildParser(vararg rules: String): Parser {
    val builder = CheckingParserBuilder()
    rules.dropLast(1).forEach { builder.def(it) }
    return builder.build(rules.last())
}

private fun buildParserDef(vararg rules: String): ParserBuilder {
    val builder = CheckingParserBuilder()
    rules.forEach { builder.def(it) }
    return builder
}

internal fun <T : ParserBuilderException> buildParserFail(
    clazz: Class<T>,
    index: Int,
    vararg rules: String,
): T {
    val throwable = assertThrows<Throwable> { buildParser(*rules) }
    try {
        return throwable
            .apply { assertThat(this).isInstanceOf(WrappedException::class.java) }
            .run { this as WrappedException }
            .also { assertThat(it.index).isEqualTo(index) }
            .apply { assertThat(source).isEqualTo(rules.last()) }
            .apply { assertThat(cause).isInstanceOf(clazz) }
            .run {
                @Suppress("UNCHECKED_CAST")
                cause as T
            }
    } catch (e: AssertionError) {
        System.err.println("Actually thrown:")
        throwable.printStackTrace(System.err)
        throw e
    }
}

internal fun <T : ParserBuilderException> buildParserFailDef(
    clazz: Class<T>,
    index: Int,
    vararg rules: String,
): T {
    val throwable = assertThrows<Throwable> { buildParserDef(*rules) }
    try {
        return throwable
            .apply { assertThat(this).isInstanceOf(WrappedException::class.java) }
            .run { this as WrappedException }
            .also { assertThat(it.index).isEqualTo(index) }
            .apply { assertThat(source).isEqualTo(rules.last()) }
            .apply { assertThat(cause).isInstanceOf(clazz) }
            .run {
                @Suppress("UNCHECKED_CAST")
                cause as T
            }
    } catch (e: AssertionError) {
        System.err.println("Actually thrown:")
        throwable.printStackTrace(System.err)
        throw e
    }
}

internal inline fun <reified T : ParserBuilderException> buildParserFail(
    index: Int,
    vararg rules: String,
): T {
    return buildParserFail(T::class.java, index, *rules)
}

internal inline fun <reified T : ParserBuilderException> buildParserFailDef(
    index: Int,
    vararg rules: String,
): T {
    return buildParserFailDef(T::class.java, index, *rules)
}