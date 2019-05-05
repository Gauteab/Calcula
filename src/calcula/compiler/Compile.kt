package calcula.compiler

import calcula.parser.Expr.*
import calcula.parser.Expr.Atom.IntExpr
import calcula.scanner.Token.*
import calcula.scanner.Token.FactorOpr.*
import calcula.scanner.Token.TermOpr.*

fun Asm.getRaxFromStack() = mov("rcx", "rax").pop("rax")

fun TermOpr.compile(asm: Asm) {
    asm.getRaxFromStack()
    when (this) {
        Plus  -> Asm::add
        Minus -> Asm::sub
    }(asm, "rax", "rcx")
}

fun FactorOpr.compile(asm: Asm) {
    asm.getRaxFromStack()
    when (this) {
        Mult -> asm.imul("rax", "rcx")
        else -> TODO()
    }
}

fun IntExpr.compile(asm: Asm) {
    asm.mov("rax", "$value")
}

fun Atom.compile(asm: Asm) {
    when (this) {
        is IntExpr -> compile(asm)
        else       -> TODO()
    }
}

fun Comparison.compile(asm: Asm) {
    terms[0].compile(asm)
    terms.drop(1).zip(oprs).forEach { (term, opr) ->
        asm.push("rax")
        term.compile(asm)
        opr.compile(asm)
    }
}

fun Asm.flagIntoRax(flag: Int) =
    pushf()
    .pop("rax")
    .shr("rax", "$flag")
    .and("rax", "1")

fun Eq.compile(asm: Asm) = asm
    .getRaxFromStack()
    .cmp("rax", "rcx")
    .flagIntoRax(6)

fun Term.compile(asm: Asm) {
    factors[0].compile(asm)
    factors.drop(1).zip(oprs).forEach { (factor, opr) ->
        asm.push("rax")
        factor.compile(asm)
        opr.compile(asm)
    }
}

fun Factor.compile(asm: Asm) {
    atoms[0].compile(asm)
    atoms.drop(1).zip(oprs).forEach { (atom, opr) ->
        asm.push("rax")
        atom.compile(asm)
        opr.compile(asm)
    }
}