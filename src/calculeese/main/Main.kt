package calculeese.main

import calculeese.main.parser.Expr
import calculeese.main.parser.parseTerm
import calculeese.main.scanner.Scanner
import calculeese.main.scanner.Token

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
        //println("Peek: ${s.curToken()}")
        val t = s.nextToken()
        println(t)
        if (t == Token.Eof) break
    }
}
