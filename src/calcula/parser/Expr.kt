package calcula.parser

import calcula.parser.Expr.*
import calcula.parser.Expr.Atom.*
import calcula.scanner.Scanner
import calcula.scanner.Token
import calcula.scanner.Token.FactorOpr
import calcula.scanner.Token.TermOpr
import kotlin.system.exitProcess


sealed class Expr {

    data class Term(val factors: List<Factor>, val oprs: List<TermOpr>) : Expr()
    data class Factor(val atoms: List<Atom>, val oprs: List<FactorOpr>) : Expr()

    sealed class Atom : Expr() {
        data class IntExpr(val value: Int)      : Atom()
        data class BoolExpr(val value: Boolean) : Atom()
        data class InnerExpr(val expr: Expr)    : Atom()
    }
}

fun parseTerm(s: Scanner): Term = log {
    val factors = mutableListOf<Factor>()
    val oprs = mutableListOf<TermOpr>()
    while (true) {
        factors += parseFactor(s)
        if (s.curToken() !is TermOpr) break
        oprs += parseTermOpr(s)
    }
    Term(factors, oprs)
}

fun parseTermOpr(s: Scanner): TermOpr = log {
    when (s.curToken()) {
        is TermOpr -> s.nextToken() as TermOpr
        else -> expectedError("{+, -}", s.curToken().toString())
    }
}

fun parseFactor(s: Scanner): Factor = log {
    val atoms = mutableListOf<Atom>()
    val oprs = mutableListOf<FactorOpr>()
    while (true) {
        atoms += parseAtom(s)
        if (s.curToken() !is FactorOpr) break
        oprs += parseFactorOpr(s)
    }
    Factor(atoms, oprs)
}

fun parseFactorOpr(s: Scanner): FactorOpr = log {
    when (s.curToken()) {
        is FactorOpr -> s.nextToken() as FactorOpr
        else -> expectedError("{*, /}", s.curToken().toString())
    }
}

fun parseAtom(s: Scanner): Atom = log {
    when (val t = s.nextToken()) {
        is Token.IntLit -> IntExpr(t.value)
        is Token.LeftPar -> parseInnerExpr(s)
        else -> expectedError("Atom", t.toString())
    }
}

fun parseInnerExpr(s: Scanner): IntExpr {
    TODO()
}

fun Scanner.skip(token: Token) {
    if (curToken() != token) expectedError(token.toString(), curToken().toString())
    nextToken()
}

fun Scanner.skip(token1: Token, token2: Token) {
    val curToken = curToken()
    if (curToken != token1 && curToken != token2)
        expectedError("{$token1, $token2}", curToken.toString())
    nextToken()
}

var indent = 0
inline fun <reified T> log(name: String = T::class.simpleName!!, block: () -> T): T {
    println("${"  ".repeat(indent++)}<$name>")
    val t = block()
    println("${"  ".repeat(--indent)}</$name>")
    return t
}

fun parseError(msg: String): Nothing {
    println("Parse Error: $msg")
    exitProcess(-1)
}

fun expectedError(expected: String, actual: String): Nothing =
    parseError("Expected `$expected`, but found `$actual`")

