package ch.bytecraft.parser.builder

import ch.bytecraft.parser.Parser
import ch.bytecraft.parser.builder.ParserBuilderException.*
import ch.bytecraft.parser.rule.ParseRule
import ch.bytecraft.parser.rule.catalog.basic.AnyRule
import ch.bytecraft.parser.rule.catalog.basic.EndOfFileRule
import ch.bytecraft.parser.rule.catalog.basic.LiteralRule
import ch.bytecraft.parser.rule.catalog.basic.OneOfRule
import ch.bytecraft.parser.rule.catalog.combined.ChoiceRule
import ch.bytecraft.parser.rule.catalog.combined.RecoveringRule
import ch.bytecraft.parser.rule.catalog.combined.RepeatedRule
import ch.bytecraft.parser.rule.catalog.combined.SequenceRule
import ch.bytecraft.parser.rule.catalog.reference.NamedRule
import ch.bytecraft.parser.rule.catalog.reference.Unwrapped
import ch.bytecraft.parser.rule.catalog.token.TokenEndRule
import ch.bytecraft.parser.rule.catalog.token.TokenRule
import ch.bytecraft.parser.rule.catalog.token.TokenStartRule

internal class ParserBuilderImpl : ParserBuilder {
    val rName = Regex("""[a-zA-Z][a-zA-Z0-9_]*(\.[a-zA-Z][a-zA-Z0-9_]*)*""")
    val rSpace = Regex(""" +""")
    val rInt = Regex("""[0-9]+""")
    val rLiteral = Regex("""([^\t\r\n']|\\['trn]|\\u\{[0-9a-fA-F]+})*""")
    val rCharClass = Regex("""([^\t\r\n\]]|\\[trn^\-\]]|\\u\{[0-9a-fA-F]+})*""")

    private val rules = mutableMapOf<String, ParseRule>()
    private val namedRules = mutableMapOf<String, NamedRule>()

    private var source = ""
    private var index = 0
    private var nextLineIndex = 0

    override fun def(source: String): ParserBuilder {
        withSource(source) {
            val isTokenRule = !skip('*')
            val name = expect(rName)
            expect(':')
            skip(rSpace)
            val rule = parseChoiceRule().let {
                it ?: throw ExpectedRuleException()
                if (isTokenRule) TokenRule(name, it) else it
            }
            if (name in rules) throw DuplicateRuleNameException(name)
            rules[name] = rule
            namedRules.computeIfAbsent(name, ::NamedRule).apply {
                this.rule = rule
                lineIndex = nextLineIndex++
            }
        }
        return this
    }

    override fun build(source: String): Parser {
        withSource(source) {
            val rule = parseChoiceRule() ?: throw ExpectedRuleException()
            val notFound = mutableSetOf<String>()
            rule.collectNamedRules(notFound)
            if (notFound.isNotEmpty()) throw UnknownRulesException(notFound)
            return Parser(rule)
        }
    }

    private fun parseChoiceRule(): ParseRule? {
        val rules = mutableListOf<ParseRule>()
        rules.add(parseRecoveringRule() ?: return null)
        while (skip('|')) {
            rules.add(parseRecoveringRule() ?: throw ExpectedRuleException())
        }
        return rules.singleOrNull() ?: ChoiceRule(rules)
    }

    private fun parseRecoveringRule(): ParseRule? {
        val prefix = parseSequenceRule()
        if (!skip(';')) return prefix
        val suffix = parseSequenceRule()
        return RecoveringRule(prefix, suffix)
    }

    private fun parseSequenceRule(): ParseRule? {
        val rules = mutableListOf<ParseRule>()
        rules.add(parseModifiedRule() ?: return null)
        while (true) {
            val rule = parseModifiedRule() ?: break
            rules.add(rule)
        }
        return rules.singleOrNull() ?: SequenceRule(rules)
    }

    private val modifierMap =
        mapOf<Char, (ParseRule) -> ParseRule>(
            '*' to { RepeatedRule(0, Int.MAX_VALUE, it) },
            '+' to { RepeatedRule(1, Int.MAX_VALUE, it) },
            '?' to { RepeatedRule(0, 1, it) },
            '{' to {
                val min = expect(rInt).toInt()
                val max = run {
                    if (!skip(',')) return@run min
                    val max = read(rInt)
                        ?: return@run Int.MAX_VALUE
                    max.toInt()
                }
                expect('}')
                RepeatedRule(min, max, it)
            },
            '[' to {
                val name = expect(rName)
                expect(']')
                TokenRule(name, it)
            },
        )

    private fun parseModifiedRule(): ParseRule? {
        var rule = parseBasicRule() ?: return null
        while (true) {
            val char = skip() ?: return rule
            val modifier = modifierMap[char] ?: run {
                index--
                return rule
            }
            rule = modifier(rule)
        }
    }

    private val basicRuleMap = mapOf(
        '(' to {
            val result = parseChoiceRule()
                ?: SequenceRule(emptyList())
            expect(')')
            result
        },
        '*' to {
            val name = expect(rName)
            Unwrapped(namedRules.computeIfAbsent(name, ::NamedRule))
        },
        '\'' to {
            val value = expect(rLiteral)
            expect('\'')
            LiteralRule(value)
        },
        '[' to {
            val complement = skip('^')
            val charClass = expect(rCharClass)
            expect(']')
            OneOfRule(complement, charClass)
        },
        '$' to { EndOfFileRule },
        '{' to { TokenStartRule },
        '}' to { TokenEndRule },
        '.' to { AnyRule },
    )

    private fun parseBasicRule(): ParseRule? {
        skip(rSpace)
        val char = skip() ?: return null
        val maker = basicRuleMap[char] ?: run {
            index--
            val name = read(rName) ?: return null
            return namedRules.computeIfAbsent(name, ::NamedRule)
        }
        return maker()
    }

    private fun expect(char: Char) {
        if (index == source.length || source[index] != char)
            throw ExpectedLiteralException(char.toString())
        index++
    }

    private fun expect(regex: Regex): String {
        val match = regex.matchAt(source, index)
            ?: throw ExpectedRegexException(regex)
        index = match.range.last + 1
        return match.value
    }

    private fun skip(): Char? {
        if (index == source.length) return null
        return source[index++]
    }

    private fun skip(char: Char): Boolean {
        if (index == source.length) return false
        if (source[index] != char) return false
        index++
        return true
    }

    private fun skip(regex: Regex): Int? {
        val match = regex.matchAt(source, index) ?: return null
        index = match.range.last + 1
        return index
    }

    private fun read(regex: Regex): String? {
        val match = regex.matchAt(source, index) ?: return null
        index = match.range.last + 1
        return match.value
    }

    private inline fun <R> withSource(source: String, block: () -> R): R {
        this.source = source
        try {
            return block()
        } catch (e: ParserBuilderException) {
            throw WrappedException(source, index, e)
        } finally {
            this.source = ""
            this.index = 0
        }
    }
}