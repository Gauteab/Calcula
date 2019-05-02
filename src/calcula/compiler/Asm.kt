package calcula.compiler

import java.lang.StringBuilder

class Asm {

    private val sb = StringBuilder()

    fun mov (s1: String, s2: String) = +"mov  $s1, $s2"
    fun push(s: String)  = +"push $s"
    fun pop (s: String)  = +"pop  $s"
    fun ret ()           = +"ret"
    fun call(s: String)  = +"call $s"

    fun imul(s1: String, s2: String) = +"imul $s1, $s2"
    fun add (s1: String, s2: String) = +"add  $s1, $s2"
    fun xor (s: String)              = +"xor  $s, $s"
    fun xor (s1: String, s2: String) = +"xor  $s1, $s2"
    fun sub (s1: String, s2: String) = +"sub  $s1, $s2"

    operator fun String.unaryPlus() = this@Asm + this
    operator fun plus(s: String)    = also { this += s      }
    operator fun plusAssign(s: String)     { sb.appendln(s) }

    override fun toString() = sb.toString()
}