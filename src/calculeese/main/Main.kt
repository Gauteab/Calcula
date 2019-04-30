package calculeese.main

import calculeese.main.parser.parseTerm
import calculeese.main.scanner.Scanner
import calculeese.main.scanner.Token

fun main() {
    val s = Scanner("calc/mini.cal")
    //while (true) {
    //    println("Peek: ${s.curToken()}")
    //    val t = s.nextToken()
    //    println(t)
    //    if (t == Token.Eof) break
    //}

    println(parseTerm(s))
}
