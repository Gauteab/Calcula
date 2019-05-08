package calcula

import calcula.Ast.Expr.BinExp
import calcula.Ast.Expr.IntExpr
import calcula.parser.scanner.Token

sealed class Ast {

    sealed class Expr : Ast() {

        data class BinExp(val left: Expr, val opr: Token, val right: Expr) : Expr()
        data class IntExpr(val value: Int) : Expr()
    }

    /**
     * Prints the AST to the console
     */
    fun print() = printTree(this, 0)
    private fun printTree(ast: Ast, indent: Int) {
        print("  ".repeat(indent))
        print("\u2502")
        print("\u2500".repeat(2))
        print(" ")
        return when (ast) {
            is IntExpr -> println(ast.value)
            is BinExp -> {
                println(ast.opr)
                printTree(ast.left, indent+1)
                printTree(ast.right, indent+1)
            }
        }
    }
}


