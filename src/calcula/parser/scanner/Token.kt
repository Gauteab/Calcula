package calcula.parser.scanner

sealed class Token {

    // Literals
    data class IntLit(val value: Int) : Token()
    //class BoolLit(val value: Boolean) : Token()

    // Operators
    sealed class TermOpr : Token() {
        object Plus  : TermOpr()
        object Minus : TermOpr()
    }

    sealed class FactorOpr : Token() {
        object Mult  : FactorOpr()
        object Div   : FactorOpr()
    }

    sealed class CompOpr : Token() {
        object Eq : CompOpr()
        object Lt : CompOpr()
        object Gt : CompOpr()
    }

    // Symbols
    object LeftPar  : Token()
    object RightPar : Token()
    object Eof      : Token()
    object Newline  : Token()

    override fun toString() = this::class.simpleName.toString()
}