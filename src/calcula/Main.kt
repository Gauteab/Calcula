package calcula

import calcula.compiler.Asm
import calcula.compiler.compile
import calcula.parser.Expr
import calcula.parser.parseExpr
import calcula.parser.parseTerm
import calcula.scanner.Scanner
import calcula.scanner.Token

var LOG = false

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    //testParser(filename)
    testCompiler(filename)
}

fun testCompiler(filename: String) = Asm().run {
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
    val e: Expr = Scanner(filename).parseExpr()
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
}
