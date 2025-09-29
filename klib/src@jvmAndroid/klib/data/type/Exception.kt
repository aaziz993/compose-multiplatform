package klib.data.type

public fun Exception.toJavaException() = java.lang.Exception(message, cause)

public fun java.lang.Exception.toKotlinException() = Exception(message, cause)
