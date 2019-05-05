package calcula.compiler

import calcula.parser.Expr.*
import calcula.parser.Expr.Atom.IntExpr
import calcula.scanner.Token
import calcula.scanner.Token.*
import calcula.scanner.Token.FactorOpr.*
import calcula.scanner.Token.TermOpr.*


fun TermOpr.compile() {
    val cmd = when (this) {
        Plus  -> "add "
        Minus -> "sub "
    }
    println("mov  rcx, rax")
    println("pop  rax")
    println("$cmd rax, rcx")
}

fun FactorOpr.compile() {
    println("mov  rcx, rax")
    println("pop  rax")
    when (this) {
        Mult -> println("imul rax, rcx")
        else -> TODO()
    }
}

fun IntExpr.compile() {
    println("mov  rax, $value")
}

fun Atom.compile() {
    when (this) {
        is IntExpr -> compile()
        else       -> TODO()
    }
}

fun Comparison.compile() {
    terms[0].compile()
    terms.drop(1).zip(oprs).forEach { (term, opr) ->
        println("push rax")
        term.compile()
        opr.compile()
    }
}

fun Eq.compile() {
    println("mov  rcx, rax")
    println("pop  rax")
    println(
"""
cmp rax, rcx
pushf
pop  rax
shr rax, 6
and rax, 1
""")
}

fun Term.compile() {
    factors[0].compile()
    factors.drop(1).zip(oprs).forEach { (factor, opr) ->
        println("push rax")
        factor.compile()
        opr.compile()
    }
}

fun Factor.compile() {
    atoms[0].compile()
    atoms.drop(1).zip(oprs).forEach { (atom, opr) ->
        println("push rax")
        atom.compile()
        opr.compile()
    }
}