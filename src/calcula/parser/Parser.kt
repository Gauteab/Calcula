package calcula.parser

import calcula.Ast.Expr
import calcula.Ast.Expr.*
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token
import calcula.parser.scanner.Token.*
import kotlin.system.exitProcess

class Parser(val sc: Scanner) {

    constructor(filename: String) : this(Scanner(filename))

    private fun curToken()  = sc.curToken()
    private fun nextToken() = sc.nextToken()

    // Binary expression
    fun binExpr(f: () -> Expr, isValidOperator: (Token) -> Boolean): Expr {

        fun opr(isValidOperator: (Token) -> Boolean): Token {
            val token = nextToken()
            if (!isValidOperator(token)) expectedError("Some Operator (TODO: Better error message)", token)
            return token
        }

        val e = f()
        if (!isValidOperator(curToken())) { return e }
        return BinExp(e, opr(isValidOperator), binExpr(f, isValidOperator))
    }


    // Expr with precedence parsing
    fun expr(): Expr = or()
    fun factor()     = binExpr(::atom)   { it is FactorOpr }
    fun term()       = binExpr(::factor) { it is TermOpr   }
    fun comp()       = binExpr(::term)   { it is CompOpr   }
    fun and()        = binExpr(::comp)   { it is And       }
    fun or()         = binExpr(::and)    { it is Or        }


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