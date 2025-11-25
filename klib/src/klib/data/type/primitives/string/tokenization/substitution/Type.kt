package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.primitives.number.toBigDecimal
import klib.data.type.primitives.number.toBigInteger
import klib.data.type.primitives.string.case.toPascalCase
import kotlin.Pair

public enum class Type(public vararg val parents: Type) {
    UNDEFINED,

    // Any
    ANY_Q,
    ANY(ANY_Q),

    // Comparable
    COMPARABLE_Q(ANY_Q),
    COMPARABLE(ANY, COMPARABLE_Q),

    // Boolean.
    BOOLEAN_Q(ANY_Q, COMPARABLE_Q),
    BOOLEAN(ANY, BOOLEAN_Q, COMPARABLE),

    // Number.
    NUMBER_Q(ANY_Q),
    NUMBER(ANY, NUMBER_Q),
    BYTE_Q(NUMBER_Q, COMPARABLE_Q),
    BYTE(NUMBER, BYTE_Q, COMPARABLE),
    SHORT_Q(NUMBER_Q, COMPARABLE_Q),
    SHORT(NUMBER, SHORT_Q, COMPARABLE),
    INT_Q(NUMBER_Q, COMPARABLE_Q),
    INT(NUMBER, INT_Q, COMPARABLE),
    LONG_Q(NUMBER_Q, COMPARABLE_Q),
    LONG(NUMBER, LONG_Q, COMPARABLE),
    FLOAT_Q(NUMBER_Q, COMPARABLE_Q),
    FLOAT(NUMBER, FLOAT_Q, COMPARABLE),
    DOUBLE_Q(NUMBER_Q, COMPARABLE_Q),
    DOUBLE(NUMBER, DOUBLE_Q, COMPARABLE),

    // Big number
    BIG_INTEGER_Q(ANY_Q),
    BIG_INTEGER(ANY, BIG_INTEGER_Q) {
        override fun invoke(vararg args: Any?): Any? = args[0]!!.toBigInteger()
    },
    BIG_DECIMAL_Q(ANY_Q),
    BIG_DECIMAL(ANY, BIG_DECIMAL_Q) {
        override fun invoke(vararg args: Any?): Any? = args[0]!!.toBigDecimal()
    },

    // Unsigned Number
    U_BYTE_Q(ANY_Q, COMPARABLE_Q),
    U_BYTE(ANY, U_BYTE_Q, COMPARABLE),
    U_SHORT_Q(ANY_Q, COMPARABLE_Q),
    U_SHORT(ANY, U_SHORT_Q, COMPARABLE),
    U_INT_Q(ANY_Q, COMPARABLE_Q),
    U_INT(ANY, U_INT_Q, COMPARABLE),
    U_LONG_Q(ANY_Q, COMPARABLE_Q),
    U_LONG(ANY, U_LONG_Q, COMPARABLE),

    // Symbolic.
    CHAR_Q(ANY_Q),
    CHAR(ANY, CHAR_Q),
    STRING_Q(ANY_Q),
    STRING(ANY, STRING_Q),

    // Pair.
    PAIR_Q(ANY_Q),
    PAIR(ANY, PAIR_Q),

    // Collection.
    ITERABLE_Q(ANY_Q),
    ITERABLE(ANY, ITERABLE_Q),
    ITERATOR_Q(ANY_Q),
    ITERATOR(ANY, ITERATOR_Q),
    ARRAY_ITR_Q(ITERATOR_Q),
    ARRAY_ITR(ITERATOR, ARRAY_ITR_Q),
    LINKED_KEY_ITERATOR_Q(ITERATOR_Q),
    LINKED_KEY_ITERATOR(ITERATOR, LINKED_KEY_ITERATOR_Q),
    COLLECTION_Q(ITERABLE_Q),
    COLLECTION(ITERABLE, COLLECTION_Q),

    // List.
    LIST_Q(COLLECTION_Q),
    LIST(COLLECTION, LIST_Q) {
        override fun invoke(vararg args: Any?): Any? = listOf(*args)
    },
    MUTABLE_LIST_Q(LIST_Q),
    MUTABLE_LIST(LIST, MUTABLE_LIST_Q),
    ARRAY_LIST_Q(MUTABLE_LIST_Q),
    ARRAY_LIST(MUTABLE_LIST, ARRAY_LIST_Q) {
        override fun invoke(vararg args: Any?): Any? = mutableListOf(*args)
    },

    // Map.
    MAP_Q(ANY_Q),
    MAP(ANY, MAP_Q) {
        @Suppress("UNCHECKED_CAST")
        override fun invoke(vararg args: Any?): Any? = mapOf(*(args.toList() as List<Pair<*, *>>).toTypedArray())
    },
    MUTABLE_MAP_Q(MAP_Q),
    MUTABLE_MAP(MAP, MUTABLE_MAP_Q),
    LINKED_HASH_MAP_Q(MUTABLE_MAP_Q),
    LINKED_HASH_MAP(MUTABLE_MAP, LINKED_HASH_MAP_Q) {
        @Suppress("UNCHECKED_CAST")
        override fun invoke(vararg args: Any?): Any? =
            mutableMapOf(*(args.toList() as List<Pair<*, *>>).toTypedArray())
    },

    // Exception.
    THROWABLE_Q(ANY_Q),
    THROWABLE(ANY, THROWABLE_Q) {
        override fun invoke(vararg args: Any?): Any? = Throwable(args[0]?.toString())
    },
    ERROR_Q(THROWABLE_Q),
    ERROR(THROWABLE, ERROR_Q),
    NOT_IMPLEMENTED_ERROR_Q(ERROR_Q),
    NOT_IMPLEMENTED_ERROR(ERROR, NOT_IMPLEMENTED_ERROR_Q) {
        override fun invoke(vararg args: Any?): Any? = NotImplementedError(args[0].toString())
    },
    EXCEPTION_Q(THROWABLE_Q),
    EXCEPTION(THROWABLE, EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = Exception(args[0].toString())
    },
    RUNTIME_EXCEPTION_Q(EXCEPTION_Q),
    RUNTIME_EXCEPTION(EXCEPTION, RUNTIME_EXCEPTION_Q),
    ILLEGAL_ARGUMENT_EXCEPTION_Q(RUNTIME_EXCEPTION_Q),
    ILLEGAL_ARGUMENT_EXCEPTION(RUNTIME_EXCEPTION, ILLEGAL_ARGUMENT_EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = IllegalArgumentException(args[0].toString())
    },
    UNSUPPORTED_OPERATION_EXCEPTION_Q(RUNTIME_EXCEPTION_Q),
    UNSUPPORTED_OPERATION_EXCEPTION(RUNTIME_EXCEPTION, UNSUPPORTED_OPERATION_EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = UnsupportedOperationException(args[0].toString())
    },
    ILLEGAL_STATE_EXCEPTION_Q(RUNTIME_EXCEPTION_Q),
    ILLEGAL_STATE_EXCEPTION(RUNTIME_EXCEPTION, ILLEGAL_ARGUMENT_EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = IllegalStateException(args[0].toString())
    },
    NULL_POINTER_EXCEPTION_Q(EXCEPTION_Q),
    NULL_POINTER_EXCEPTION(EXCEPTION, NULL_POINTER_EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = NullPointerException(args[0].toString())
    },
    NO_SUCH_ELEMENT_EXCEPTION_Q(RUNTIME_EXCEPTION_Q),
    NO_SUCH_ELEMENT_EXCEPTION(RUNTIME_EXCEPTION, NO_SUCH_ELEMENT_EXCEPTION_Q) {
        override fun invoke(vararg args: Any?): Any? = NoSuchElementException(args[0].toString())
    },
    INDEX_OUT_OF_BOUNDS_EXCEPTION_Q(RUNTIME_EXCEPTION_Q),
    INDEX_OUT_OF_BOUNDS_EXCEPTION(RUNTIME_EXCEPTION, INDEX_OUT_OF_BOUNDS_EXCEPTION_Q);

    public val type: String = name.removeSuffix("_Q").toPascalCase().let { base ->
        if (name.endsWith("_Q")) "$base?" else base
    }

    public val nullable: Boolean = name.endsWith("_Q")

    public infix fun check(value: Any?) {
        if (this === UNDEFINED) return
        if (value == null)
            return require(nullable) { "Cannot cast null to '$type'" }

        val valueType = value::class.simpleName!!.toType()

        require(this isAssignableFrom valueType) {
            "Cannot cast ${valueType.type} to '$type'"
        }
    }

    public open operator fun invoke(vararg args: Any?): Any? = throw UnsupportedOperationException()

    public infix fun isSubtypeOf(other: Type): Boolean {
        if (this === other) return true
        val seen = HashSet<Type>()
        val queue = ArrayDeque<Type>()
        queue += this
        while (queue.isNotEmpty()) {
            val t = queue.removeFirst()
            if (!seen.add(t)) continue
            if (other in t.parents) return true
            queue.addAll(t.parents)
        }
        return false
    }

    public infix fun isAssignableFrom(other: Type): Boolean = other isSubtypeOf this
}

public fun String.toTypeOrNull(): Type? = Type.entries.firstOrNull { it.type == this }

public fun String.toType(): Type = toTypeOrNull() ?: error("Unknown type '$this'")
