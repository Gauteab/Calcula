package calcula

import calcula.parser.scanner.Token

/**
 * AST Class For Expression Language
 * Used as Algebraic Data Type
 */
sealed class Expr {

    data class BinExp(val left: Expr, val opr: Token, val right: Expr) : Expr()
    data class Primary(val opr: Token?, val e: Expr) : Expr()
    data class IntExpr(val value: Int) : Expr()
    data class BoolExpr(val value: Boolean) : Expr()

    /**
     * Prints the AST to the console
     */
    fun print() = printTree(this, 0)
    private fun printTree(e: Expr, indent: Int, pre: Boolean = true) {
        if (pre) {
            print("  ".repeat(indent))
            print("\u2502")
            print("\u2500".repeat(2))
            print(" ")
        }
        return when (e) {
            is IntExpr  -> println(e.value)
            is BoolExpr -> println(if (e.value) "T" else "F")
            is BinExp   -> {
                println(e.opr)
                printTree(e.left, indent+1)
                printTree(e.right, indent+1)
            }
            is Primary -> {
                if (e.opr != null) print(e.opr)
                printTree(e.e, indent, false)

            }
        }
    }
}


