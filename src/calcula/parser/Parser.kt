package calcula.parser

import calcula.Expr
import calcula.Expr.*
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token
import calcula.parser.scanner.Token.*
import kotlin.system.exitProcess

// Binary expression
fun Scanner.binExpr(f: () -> Expr, isValidOperator: (Token) -> Boolean): Expr {

    fun Scanner.collect(es: List<Expr>, os: List<Token>): Expr =
        if (os.isEmpty()) es[0]
        else              BinExp(collect(es.dropLast(1), os.dropLast(1)), os.last(), es.last())

    val es = mutableListOf<Expr>(f())
    val os = mutableListOf<Token>()

    while (true) {
        os += curToken().takeIf(isValidOperator) ?: break
        nextToken()
        es += f()
    }

    return collect(es, os)
}

// Expr with precedence parsing
fun Scanner.expr(): Expr = or().also {
    // Check that the scanner is finished
    var t = nextToken()
    while (t == Newline) { t = nextToken() }
    if (t != Eof) parseError("Unexpected token: $t")
}

fun Scanner.or()         = binExpr(::and)     { it is Or         }
fun Scanner.and()        = binExpr(::comp)    { it is And        }
fun Scanner.comp()       = binExpr(::term,    Token::isCompOpr   )
fun Scanner.term()       = binExpr(::factor,  Token::isTermOpr   )
fun Scanner.factor()     = binExpr(::primary, Token::isFactorOpr )

fun Scanner.primary(): Expr {
    val opr = curToken().takeIf(Token::isUnaryOpr) ?: return atom()
    nextToken()
    return Primary(opr, primary())
}

fun Scanner.atom(): Expr = when (curToken()) {
    is IntLit  -> int()
    is BoolLit -> bool()
    is LeftPar -> innerExpr()
    else       -> expectedError("Atom", curToken())
}

fun Scanner.innerExpr(): Expr {
    skip(LeftPar)
    val e = expr()
    skip(RightPar)
    return e
}

fun Scanner.int(): IntExpr {
    val token = nextToken()
    require(token is IntLit)
    return IntExpr(token.value)
}

fun Scanner.bool(): BoolExpr {
    val token = nextToken()
    require(token is BoolLit)
    return BoolExpr(token.value)
}

/**
 * Helpers
 */

fun Scanner.skip(token: Token) {
    if (curToken() != token) expectedError(token.toString(), curToken().toString())
    nextToken()
}

/**
 * Errors
 */

fun parseError(msg: String): Nothing {
    println("Parse Error!\n$msg")
    exitProcess(-1)
}

fun expectedError(expected: String, actual: Token): Nothing = expectedError(expected, actual.toString())

fun expectedError(expected: String, actual: String): Nothing = parseError("Expected `$expected`, but found `$actual`")

