package calcula.parser

import calcula.Ast.Expr
import calcula.Ast.Expr.*
import calcula.Ast.Expr.Atom.*
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token
import calcula.parser.scanner.Token.*
import kotlin.system.exitProcess

class Parser(val sc: Scanner) {

    constructor(filename: String) : this(Scanner(filename))

    fun curToken()  = sc.curToken()
    fun nextToken() = sc.nextToken()

    // Binary expression
    fun <T: Token> binExpr(f: () -> Expr, clazz: Class<out T>): Expr {

        fun opr(clazz: Class<out Token>): Token {
            val token = nextToken()
            if (!clazz.isInstance(token)) expectedError(clazz.simpleName, token)
            return token
        }

        val e = f()
        if (!clazz.isInstance(curToken())) { return e }
        return BinExp(e, opr(clazz), binExpr(f, clazz))
    }


    // Expr with precedence parsing
    fun expr(): Expr = or()
    fun factor()     = binExpr(::atom,   FactorOpr::class.java)
    fun term()       = binExpr(::factor, TermOpr::class.java)
    fun comp()       = binExpr(::term,   CompOpr::class.java)
    fun and()        = binExpr(::comp,   And::class.java)
    fun or()         = binExpr(::and,    Or::class.java)


    fun atom(): Expr = when (curToken()) {
        is IntLit  -> int()
        is LeftPar -> innerExpr()
        else       -> expectedError("Atom", curToken())
    }

    fun innerExpr(): Expr {
        sc.skip(LeftPar)
        val e = expr()
        sc.skip(RightPar)
        return e
    }

    fun int(): IntExpr {
        val token = nextToken()
        require(token is IntLit)
        return IntExpr(token.value)
    }

    /**
     * Helpers
     */

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

    /**
     * Errors
     */

    fun parseError(msg: String): Nothing {
        println("Parse Error: $msg")
        exitProcess(-1)
    }

    fun expectedError(expected: String, actual: Token): Nothing = expectedError(expected, actual.toString())

    fun expectedError(expected: String, actual: String): Nothing = parseError("Expected `$expected`, but found `$actual`")

}