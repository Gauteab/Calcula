package calcula

import calcula.parser.scanner.Token

sealed class Ast {

    sealed class Expr : Ast() {

        data class BinExp(val e1: Expr, val opr: Token, val e2: Expr) : Expr()

        sealed class Atom : Expr() {
            data class IntExpr(val value: Int) : Atom()
        }
    }
}


