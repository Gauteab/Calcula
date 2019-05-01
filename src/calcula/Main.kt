package calcula

import calcula.parser.Expr
import calcula.parser.parseTerm
import calcula.scanner.Scanner
import calcula.scanner.Token

fun main() {
    val filename = "calc/mini.cal"
    testParser(filename)
    println("Done")
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
