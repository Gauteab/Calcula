package calcula.parser.scanner

import calcula.parser.scanner.Token.*
import calcula.parser.scanner.Token.CompOpr.*
import calcula.parser.scanner.Token.FactorOpr.*
import calcula.parser.scanner.Token.TermOpr.*
import java.io.File
import java.util.*
import kotlin.system.exitProcess

class Scanner(filename: String) {

    private val input  = File(filename).bufferedReader().lines().iterator()
    private val tokens = LinkedList<Token>()

    fun curToken(): Token {
        while (tokens.isEmpty()) readLine()
        return tokens.peekFirst()
    }

    fun nextToken(): Token {
        while (tokens.isEmpty()) readLine()
        return tokens.removeFirst()
    }

    private fun readLine() =
        if (!input.hasNext())
            tokens.push(Eof)
        else
            tokenize(input.next())

    private fun tokenize(s: String) {
        var i = 0
        if (s.isBlank() || s.isEmpty()) return
        while (i < s.length) {
            val (token, offset) = when (s[i]) {
                ' ' ,
                '\t',
                '\r' -> null     to 1
                //'\n' -> Newline to 1
                '+'  -> Plus     to 1
                '-'  -> Minus    to 1
                '*'  -> Mult     to 1
                '/'  -> Div      to 1
                '='  -> Eq       to 1
                '>'  -> Gt       to 1
                '<'  -> Lt       to 1
                '('  -> LeftPar  to 1
                ')'  -> RightPar to 1
                in '0' .. '9' -> scanIntLit(s, i)
                else          -> scannerError("Unrecognized Symbol: ${s[i]}")
            }
            if (token != null) tokens.addLast(token)
            i += offset
        }
        tokens.addLast(Newline)
    }

    fun scanIntLit(s: String, i: Int): Pair<Token, Int> {
        var x = i
        var result = ""
        while (s[x] in '0' .. '9') {
            result += s[x++]
            if (x >= s.length) break
        }
        return IntLit(result.toInt()) to x-i
    }

    private fun scannerError(msg: String = "Yikes!"): Nothing {
        println("Scanner Error:\n$msg")
        exitProcess(-1)
    }
}