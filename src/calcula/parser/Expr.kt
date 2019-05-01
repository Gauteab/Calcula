package calcula.parser

import calcula.scanner.Token.FactorOpr
import calcula.scanner.Token.TermOpr


sealed class Expr {

    data class Term(val factors: List<Factor>, val oprs: List<TermOpr>) : Expr()
    data class Factor(val atoms: List<Atom>, val oprs: List<FactorOpr>) : Expr()

    sealed class Atom : Expr() {
        data class IntExpr(val value: Int)      : Atom()
        data class BoolExpr(val value: Boolean) : Atom()
        data class InnerExpr(val expr: Expr)    : Atom()
    }
}

