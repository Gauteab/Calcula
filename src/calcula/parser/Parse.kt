package calcula.parser

import calcula.LOG
import calcula.scanner.Scanner
import calcula.scanner.Token
import kotlin.system.exitProcess

fun Scanner.parseExpr() = parseComparison()

fun Scanner.parseComparison(): Expr.Comparison = log {
    val terms = mutableListOf<Expr.Term>()
    val oprs  = mutableListOf<Token.CompOpr>()
    while (true) {
        terms += parseTerm()
        if (curToken() !is Token.CompOpr) break
        oprs += parseCompOpr()
    }
    Expr.Comparison(terms, oprs)
}

fun Scanner.parseCompOpr(): Token.CompOpr = log {
    if (curToken() !is Token.CompOpr) expectedError("-", curToken().toString())
    nextToken() as Token.CompOpr
}

fun Scanner.parseTerm(): Expr.Term = log {
    val factors = mutableListOf<Expr.Factor>()
    val oprs = mutableListOf<Token.TermOpr>()
    while (true) {
        factors += parseFactor()
        if (curToken() !is Token.TermOpr) break
        oprs += parseTermOpr()
    }
    Expr.Term(factors, oprs)
}

fun Scanner.parseTermOpr(): Token.TermOpr = log {
    when (curToken()) {
        is Token.TermOpr -> nextToken() as Token.TermOpr
        else -> expectedError("{+, -}", curToken().toString())
    }
}

fun Scanner.parseFactor(): Expr.Factor = log {
    val atoms = mutableListOf<Expr.Atom>()
    val oprs = mutableListOf<Token.FactorOpr>()
    while (true) {
        atoms += parseAtom()
        if (curToken() !is Token.FactorOpr) break
        oprs += parseFactorOpr()
    }
    Expr.Factor(atoms, oprs)
}

fun Scanner.parseFactorOpr(): Token.FactorOpr = log {
    when (curToken()) {
        is Token.FactorOpr -> nextToken() as Token.FactorOpr
        else -> expectedError("{*, /}", curToken().toString())
    }
}

fun Scanner.parseAtom(): Expr.Atom = log {
    when (val t = nextToken()) {
        is Token.IntLit -> Expr.Atom.IntExpr(t.value)
        is Token.LeftPar -> parseInnerExpr()
        else -> expectedError("Atom", t.toString())
    }
}

fun Scanner.parseInnerExpr(): Expr.Atom.IntExpr {
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
inline fun <reified T> log(name: String = T::class.simpleName ?: "<T::classimpleName>", block: () -> T): T {
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