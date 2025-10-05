package klib.processing.model

internal class Error {
    companion object Companion {

        const val FUNCTION_OR_PARAMETERS_TYPES_MUST_NOT_INCLUDE_ATYPE_VARIABLE_OR_WILDCARD =
            "function or parameters types must not include a type variable or wildcard:"
        const val JAVA_INTERFACES_ARE_NOT_SUPPORTED = "Java Interfaces are not supported"
        const val INTERFACE_NEEDS_TO_HAVE_A_PACKAGE = "Interface needs to have a package"
        const val ONLY_ONE_ENCODING_ANNOTATION_IS_ALLOWED = "Only one encoding annotation is allowed."
        const val TYPE_PARAMETERS_ARE_UNSUPPORTED_ON = "Type parameters are unsupported on "
        const val VARARG_NOT_SUPPORTED_USE_LIST_OR_ARRAY = "vararg not supported use List or Array"
        const val PROPERTIES_NOT_SUPPORTED = "throw IllegalStateException(\"Properties not supported by Compiler\")"

        fun missingEitherKeywordUrlOrUrlParameter(keyword: String) = "Missing either @$keyword URL or @Url parameter"

        fun missingXInRelativeUrlPath(keyword: String) = "Missing {$keyword} in relative url path"

        fun noHttpAnnotationAt(functionName: String) = "No Http annotation at $functionName"

        fun urlCanOnlyBeUsedWithEmpty(keyword: String) = "@Url can only be used with empty @$keyword URL value"
    }
}