package calcula.compiler

import calcula.Expr
import calcula.Expr.*
import calcula.parser.scanner.Token.*
import kotlin.system.exitProcess

fun Asm.flagIntoRax(flag: Int) = run {
    pushf()
    pop("rax")
    shr("rax", "$flag")
    and("rax", "1")
}

fun Asm.compile(e: Expr): Any = when (e) {
    is BinExp  -> compile(e)
    is IntExpr -> mov("rax", "${e.value}")
    is Primary -> TODO()
}

fun Asm.compile(e: BinExp) {
    compile(e.left)
    push("rax")
    compile(e.right)
    mov("rcx", "rax")
    pop("rax")
    when (e.opr) {
        Plus  -> add("rax", "rcx")
        Minus -> sub("rax", "rcx")
        Mult  -> imul("rax", "rcx")
        Div   -> { xor("rdx"); idiv("rcx") }
        And   -> and("rax", "rcx")
        Or    -> or("rax", "rcx")
        Eq    -> { cmp("rcx", "rax"); flagIntoRax(6) }
        Lt    -> { cmp("rcx", "rax"); flagIntoRax(7) }
        Gt    -> { cmp("rax", "rcx"); flagIntoRax(7) }
        //Not   -> TODO: Implement unary operators
        else -> compileError("Unsupported binary operator: ${e.opr}")
    }
}

fun compileError(msg: String): Nothing {
    println("Compile Error!\n$msg")
    exitProcess(-1)
}
