package calcula.parser

import calcula.Ast.Expr.*
import calcula.LOG
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token
import calcula.parser.scanner.Token.CompOpr
import kotlin.system.exitProcess

fun Scanner.parseExpr() = parseComparison()

fun Scanner.parseComparison(): Comparison = log {
    val terms = mutableListOf<Term>()
    val oprs  = mutableListOf<CompOpr>()
    while (true) {
        terms += parseTerm()
        if (curToken() !is CompOpr) break
        oprs += parseCompOpr()
    }
    Comparison(terms, oprs)
}

fun Scanner.parseCompOpr(): CompOpr = log {
    if (curToken() !is CompOpr) expectedError("-", curToken().toString())
    nextToken() as CompOpr
}

fun Scanner.parseTerm(): Term = log {
    val factors = mutableListOf<Factor>()
    val oprs = mutableListOf<Token.TermOpr>()
    while (true) {
        factors += parseFactor()
        if (curToken() !is Token.TermOpr) break
        oprs += parseTermOpr()
    }
    Term(factors, oprs)
}

fun Scanner.parseTermOpr(): Token.TermOpr = log {
    when (curToken()) {
        is Token.TermOpr -> nextToken() as Token.TermOpr
        else -> expectedError("{+, -}", curToken().toString())
    }
}

fun Scanner.parseFactor(): Factor = log {
    val atoms = mutableListOf<Atom>()
    val oprs = mutableListOf<Token.FactorOpr>()
    while (true) {
        atoms += parseAtom()
        if (curToken() !is Token.FactorOpr) break
        oprs += parseFactorOpr()
    }
    Factor(atoms, oprs)
}

fun Scanner.parseFactorOpr(): Token.FactorOpr = log {
    when (curToken()) {
        is Token.FactorOpr -> nextToken() as Token.FactorOpr
        else -> expectedError("{*, /}", curToken().toString())
    }
}

fun Scanner.parseAtom(): Atom = log {
    when (val t = nextToken()) {
        is Token.IntLit -> Atom.IntExpr(t.value)
        is Token.LeftPar -> parseInnerExpr()
        else -> expectedError("Atom", t.toString())
    }
}

fun Scanner.parseInnerExpr(): Atom.IntExpr {
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