package calcula.compiler

import calcula.Ast.Expr
import calcula.Ast.Expr.*
import calcula.parser.scanner.Token.*
import calcula.parser.scanner.Token.FactorOpr.*
import calcula.parser.scanner.Token.TermOpr.*

fun Asm.getRaxFromStack() = mov("rcx", "rax").pop("rax")

fun Asm.flagIntoRax(flag: Int) = run {
    pushf()
    pop("rax")
    shr("rax", "$flag")
    and("rax", "1")
}


fun Expr.compile(asm: Asm): Any = when (this) {
    is BinExp  -> compile(asm)
    is IntExpr -> asm.mov("rax", "$value")
}

fun BinExp.compile(asm: Asm) {
    e1.compile(asm)
    asm.push("rax")
    e2.compile(asm)
    asm.mov("rcx", "rax")
    asm.pop("rax")
    with (asm) {
        when (opr) {
            Plus  -> add("rax", "rcx")
            Minus -> sub("rax", "rcx")
            Mult  -> imul("rax", "rcx")
            Div   -> xor("rdx").idiv("rcx")
/*          Eq    -> Asm::
            Lt    -> Asm::
            Gt    -> Asm::*/
            And   -> and("rax", "rcx")
            Or    -> or("rax", "rcx")
            //Not   -> Asm::
            else  -> TODO()
        }
    }
}
