package calcula

import calcula.compiler.compile
import calcula.parser.Expr
import calcula.parser.parseTerm
import calcula.scanner.Scanner
import calcula.scanner.Token

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    listOf<String>().drop(1)
    //testParser(filename)
    testCompiler(filename)
}

fun testCompiler(filename: String) {
    val s = Scanner(filename)
    println("  global main")
    println("  extern printf")
    println("  section .text")
    println("main:")
    val term = parseTerm(s)
    term.compile()

    println()
    println("mov  rdi, fmt")
    println("mov  rsi, rax")
    println("xor  rax, rax")
    println("call printf")
    println("ret")
    println("""fmt: db `%d\n`, 0""")
}

fun testParser(filename: String) {
    val s = Scanner(filename)
    val e: Expr = parseTerm(s)
    println(e)
}

fun testScanner(filename: String) {
    val s = Scanner(filename)
    while (true) {
        val t = s.nextToken()
        println(t)
        if (t == Token.Eof) break
    }
}
