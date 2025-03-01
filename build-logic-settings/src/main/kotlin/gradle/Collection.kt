package gradle

import com.github.h0tk3y.betterParse.combinators.*
import com.github.h0tk3y.betterParse.grammar.Grammar
import com.github.h0tk3y.betterParse.grammar.parser
import com.github.h0tk3y.betterParse.lexer.literalToken
import com.github.h0tk3y.betterParse.lexer.regexToken
import com.github.h0tk3y.betterParse.parser.Parser

internal fun Any.resolve(): Any? = when (this) {
    is String -> resolveValue()
    is Map<*, *> -> mapValues { (_, value) -> value?.resolve() }
    is List<*> -> map { it?.resolve() }
    else -> this
}

internal fun String.resolveValue(): Any {
    return this
}

//private class Resolver() : Grammar<Int>() {
//
//    private val LBRC by literalToken("\\{")
//    private val RBRC by literalToken("}")
//    val NON_EVAL by regexToken("""([^$\\]*(?:\\{2})*(?:\\[^$])?)*""")
//
//    val DOLLAR_SIGN by literalToken("$")
//
//    val EXT_ID by regexToken("[\\w.]+")
//
//    val EXT_ID_REF by -DOLLAR_SIGN * EXT_ID
//
////    val braced by -LBRC *  * -RBRC
//
//    override val rootParser: Parser<Int> = null
//}
