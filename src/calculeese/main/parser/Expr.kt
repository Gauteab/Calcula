package calculeese.main.parser

import calculeese.main.parser.Expr.*
import calculeese.main.parser.Expr.Atom.*
import calculeese.main.scanner.Scanner
import calculeese.main.scanner.Token
import kotlin.system.exitProcess


sealed class Expr {

    class Term(val factors: List<Factor>, val oprs: List<TermOpr>) : Expr()
    class TermOpr(val token: Token)

    class Factor(val atoms: List<Atom>, val oprs: List<FactorOpr>)
    class FactorOpr(val token: Token)

    sealed class Atom : Expr() {
        class IntExpr(val value: Int) : Atom()
        class BoolExpr(val value: Boolean) : Atom()
        class InnerExpr(val expr: Expr) : Atom()
    }
}

fun parseTerm(s: Scanner): Term = log {
    Term(listOf(), listOf())
}

fun parseTermOpr(s: Scanner): TermOpr {
    TODO()
}

fun parseFactor(s: Scanner): Factor {
    TODO()
}

fun parseFactorOpr(s: Scanner): FactorOpr {
    TODO()
}

fun parseAtom(s: Scanner): Atom = when (val t = s.curToken()) {
    is Token.IntLit  -> IntExpr(t.value)
    is Token.LeftPar -> parseInnerExpr(s)
    else             -> parseError("Atom")
}

fun parseInnerExpr(s: Scanner): IntExpr {
    TODO()
}


var indent = 0
inline fun <reified T> log(name: String = T::class.simpleName!!, block: () -> T): T {
    println("${"  ".repeat(indent++)}<$name>")
    val t = block()
    println("${"  ".repeat(--indent)}</$name>")
    return t
}

fun parseError(msg: String): Nothing {
    println("Parse Error: $msg")
    exitProcess(-1)
}

