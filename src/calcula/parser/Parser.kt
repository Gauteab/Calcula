package calcula.parser

import calcula.Ast.Expr
import calcula.Ast.Expr.*
import calcula.Ast.Expr.Atom.*
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token
import calcula.parser.scanner.Token.*
import kotlin.system.exitProcess

class Parser(val sc: Scanner, val log: Boolean = false) {

    fun curToken()  = sc.curToken()
    fun nextToken() = sc.nextToken()

    fun expr(): Expr = log("expr") {
        comp()
    }

    fun <T: Token> binExpr(f: () -> Expr, clazz: Class<out T>): Expr = log(clazz.simpleName) {

        fun opr(clazz: Class<out Token>): Token = log(clazz.simpleName) {
            val token = nextToken()
            if (!clazz.isInstance(token)) expectedError(clazz.simpleName, token)
            token
        }

        val e = f()
        if (!clazz.isInstance(curToken())) { return e }
        BinExp(e, opr(clazz), binExpr(f, clazz))
    }

    fun factor() = binExpr(::atom,   FactorOpr::class.java)
    fun term()   = binExpr(::factor, TermOpr::class.java)
    fun comp()   = binExpr(::term,   CompOpr::class.java)


    fun atom(): Expr = log("atom") {
        when (curToken()) {
            is IntLit  -> int()
            is LeftPar -> innerExpr()
            else       -> expectedError("Atom", curToken())
        }
    }

    fun innerExpr(): Expr = log("inner-expr") {
        sc.skip(LeftPar)
        val e = expr()
        sc.skip(RightPar)
        e
    }

    fun int(): IntExpr = log("int") {
        val token = nextToken()
        require(token is IntLit)
        IntExpr(token.value)
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
        if (!log) return block()
        println("${"   ".repeat(indent++)}<$name>")
        val t = block()
        println("${"   ".repeat(--indent)}</$name>")
        return t
    }

    fun parseError(msg: String): Nothing {
        println("Parse Error: $msg")
        exitProcess(-1)
    }

    fun expectedError(expected: String, actual: Token): Nothing =
        expectedError(expected, actual.toString())
    fun expectedError(expected: String, actual: String): Nothing =
        parseError("Expected `$expected`, but found `$actual`")
}