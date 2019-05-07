package calcula

import calcula.parser.Parser

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    testParser(filename)
}

fun testParser(filename: String) =
    Parser(filename)
    .expr()
    .print()

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
*/

