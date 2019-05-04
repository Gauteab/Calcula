package calcula.parser

import calcula.LOG
import calcula.scanner.Scanner
import calcula.scanner.Token
import kotlin.system.exitProcess

fun parseExpr(s: Scanner) = parseComparison(s)

fun parseComparison(s: Scanner): Expr.Comparison = log {
    val terms = mutableListOf<Expr.Term>()
    val oprs  = mutableListOf<Token.Eq>()
    while (true) {
        terms += parseTerm(s)
        if (s.curToken() !is Token.Eq) break
        oprs += parseCompOpr(s)
    }
    Expr.Comparison(terms, oprs)
}

fun parseCompOpr(s: Scanner): Token.Eq = log {
    if (s.curToken() !is Token.Eq) expectedError("-", s.curToken().toString())
    s.nextToken() as Token.Eq
}

fun parseTerm(s: Scanner): Expr.Term = log {
    val factors = mutableListOf<Expr.Factor>()
    val oprs = mutableListOf<Token.TermOpr>()
    while (true) {
        factors += parseFactor(s)
        if (s.curToken() !is Token.TermOpr) break
        oprs += parseTermOpr(s)
    }
    Expr.Term(factors, oprs)
}

fun parseTermOpr(s: Scanner): Token.TermOpr = log {
    when (s.curToken()) {
        is Token.TermOpr -> s.nextToken() as Token.TermOpr
        else -> expectedError("{+, -}", s.curToken().toString())
    }
}

fun parseFactor(s: Scanner): Expr.Factor = log {
    val atoms = mutableListOf<Expr.Atom>()
    val oprs = mutableListOf<Token.FactorOpr>()
    while (true) {
        atoms += parseAtom(s)
        if (s.curToken() !is Token.FactorOpr) break
        oprs += parseFactorOpr(s)
    }
    Expr.Factor(atoms, oprs)
}

fun parseFactorOpr(s: Scanner): Token.FactorOpr = log {
    when (s.curToken()) {
        is Token.FactorOpr -> s.nextToken() as Token.FactorOpr
        else -> expectedError("{*, /}", s.curToken().toString())
    }
}

fun parseAtom(s: Scanner): Expr.Atom = log {
    when (val t = s.nextToken()) {
        is Token.IntLit -> Expr.Atom.IntExpr(t.value)
        is Token.LeftPar -> parseInnerExpr(s)
        else -> expectedError("Atom", t.toString())
    }
}

fun parseInnerExpr(s: Scanner): Expr.Atom.IntExpr {
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
inline fun <reified T> log(name: String = T::class.simpleName ?: "<T::class.simpleName>", block: () -> T): T {
    if (!LOG) return block()
    println("${"   ".repeat(indent++)}<$name>")
    val t = block()
    println("${"   ".repeat(--indent)}</$name>")
    return t
}

fun parseError(msg: String): Nothing {
    println("Parse Error: $msg")
    exitProcess(-1)
}

fun expectedError(expected: String, actual: String): Nothing =
    parseError("Expected `$expected`, but found `$actual`")