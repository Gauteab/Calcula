package calcula

import calcula.Ast.Expr.Atom.IntExpr
import calcula.Ast.Expr.BinExp
import calcula.parser.Parser
import calcula.parser.scanner.Scanner
import calcula.parser.scanner.Token.*

fun main(args: Array<String>) {
    val filename = args.firstOrNull() ?: "calc/mini.cal"
    testParser(filename)
}

fun testParser(filename: String) {
    val e = Parser(filename).expr()
    tree(e)
}

fun tree(ast: Ast, indent: Int = 0) {
    print("  ".repeat(indent))
    print("\u2502")
    print("\u2500".repeat(2))
    return when (ast) {
        is IntExpr -> println(ast.value)
        is BinExp  -> {
            println(ast.opr)
            tree(ast.e1, indent+1)
            tree(ast.e2, indent+1)
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
*/

fun testScanner(filename: String) {
    val s = Scanner(filename)
    while (true) {
        val t = s.nextToken()
        println(t)
        if (t == Eof) break
    }
}
