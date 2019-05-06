package calcula.compiler

import calcula.Ast.Expr.*
import calcula.Ast.Expr.Atom.IntExpr
import calcula.parser.scanner.Token.*
import calcula.parser.scanner.Token.CompOpr.*
import calcula.parser.scanner.Token.FactorOpr.*
import calcula.parser.scanner.Token.TermOpr.*

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

fun CompOpr.compile(asm: Asm) = asm.run {
    getRaxFromStack()
    when (this@compile) {
        is Eq, Lt -> asm.cmp("rax", "rcx")
        is Gt     -> asm.cmp("rcx", "rax")
    }
    flagIntoRax(when (this@compile) {
        Eq     -> 6
        Lt, Gt -> 7
    })
}

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