package calcula.parser.scanner

sealed class Token(val image: String? = null) {

    // Literals
    data class IntLit(val value: Int) : Token("Int")
    //class BoolLit(val value: Boolean) : Token()

    // Operators
    object Plus  : Token("+")
    object Minus : Token("-")

    object Mult  : Token("*")
    object Div   : Token("/")

    object Eq    : Token("=")
    object Lt    : Token("<")
    object Gt    : Token(">")

    object And   : Token("&")
    object Or    : Token("|")
    object Not   : Token("!")

    // Symbols
    object LeftPar  : Token("(")
    object RightPar : Token(")")
    object Eof      : Token()
    object Newline  : Token()

    override fun toString() = image ?: this::class.simpleName.toString()

    fun isCompOpr()   = this in arrayOf(Eq, Lt, Gt)
    fun isTermOpr()   = this in arrayOf(Plus, Minus)
    fun isFactorOpr() = this in arrayOf(Mult, Div)
    fun isUnaryOpr()  = this in arrayOf(Not, Minus)
}