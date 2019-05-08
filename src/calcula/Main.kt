package calcula

import calcula.compiler.Asm
import calcula.compiler.compile
import calcula.parser.Parser
import calcula.parser.scanner.Scanner

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    //testParser(filename)
    testCompiler(filename)
}

fun testParser(filename: String) =
    Parser(filename)
    .expr()
    .print()

fun testCompiler(filename: String) = Asm().run {
    global("main")
    extern("printf")
    section("text")
    label("main")
    compile(Parser(Scanner(filename)).expr())
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

