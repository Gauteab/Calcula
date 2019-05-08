package calcula.compiler

import java.lang.StringBuilder

class Asm {

    private val sb = StringBuilder()

    fun mov (s1: String, s2: String) = +"  mov  $s1, $s2"
    fun push(s: String)  = +"  push $s"
    fun pop (s: String)  = +"  pop  $s"
    fun ret ()           = +"  ret"
    fun call(s: String)  = +"  call $s"

    fun imul(s1: String, s2: String) = +"  imul $s1, $s2"
    fun idiv(s: String)              = +"  idiv $s"
    fun add (s1: String, s2: String) = +"  add  $s1, $s2"
    fun xor (s: String)              = +"  xor  $s, $s"
    fun xor (s1: String, s2: String) = +"  xor  $s1, $s2"
    fun neg(s: String)               = +"  neg  $s"
    fun sub (s1: String, s2: String) = +"  sub  $s1, $s2"
    fun cmp(s1: String, s2: String)  = +"  cmp  $s1, $s2"
    fun pushf()                      = +"  pushf"
    fun shr(s1: String, s2: String)  = +"  shr  $s1, $s2"
    fun and(s1: String, s2: String)  = +"  and  $s1, $s2"
    fun or(s1: String, s2: String)   = +"  or   $s1, $s2"
    fun raw(s: String)               = +s
    fun label(name: String)          = +"$name:"
    fun global(name: String)         = +"  global $name"
    fun extern(name: String)         = +"  extern $name"
    fun section(name: String)        = +"  section .$name"
    fun data(name: String, size: String, data: String)
            = +"$name: $size $data"

    fun nl()                         = +""

    operator fun String.unaryPlus()     = plusAssign(this)
    operator fun plusAssign(s: String)  { sb.appendln(s) }

    override fun toString() = sb.toString()

}