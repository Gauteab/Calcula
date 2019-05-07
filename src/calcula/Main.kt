package calcula

import calcula.Ast.Expr
import calcula.Ast.Expr.Atom.IntExpr
import calcula.Ast.Expr.BinExp
import calcula.parser.Parser
import calcula.parser.scanner.Scanner

var LOG = true

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    testParser(filename)
}

fun testParser(filename: String) {
    val sc = Scanner(filename)
    val parser = Parser(sc)
    val e = parser.expr()
    println(e)
    tree(e)
}

val h = "\u2500"
val v = "\u2502"

fun tree(e: Expr, indent: Int = 0) {
    //print(" ".repeat(indent))
    //print("\u2500".repeat(2))
    print("  ".repeat(indent))
    print(v)
    print(h.repeat(2))
    when (e) {
        is IntExpr -> println(e.value)
        is BinExp  -> {
            println(e.opr)
            tree(e.e1, indent+1)
            tree(e.e2, indent+1)
        }
    }
}

/*fun testCompiler(filename: String) = Asm().run {
    global("main")
    extern("printf")
    section("text")
    label("main")
    Scanner(filename).parseExpr().compile(this)
    nl()
    mov("rdi", "format")
    mov("rsi", "rax")
    xor("rax")
    call("printf")
    ret()
    nl()
    section("data")
    data("format", "db", "\"%d\", 10, 0")
    println(this)
}

fun testParser(filename: String) {
    LOG = true
    val e: Ast.Expr = Scanner(filename).parseExpr()
    LOG = false
    println(e)
    println()
}

fun testScanner(filename: String) {
    val s = Scanner(filename)
    while (true) {
        val t = s.nextToken()
        println(t)
        if (t == Token.Eof) break
    }
}*/
