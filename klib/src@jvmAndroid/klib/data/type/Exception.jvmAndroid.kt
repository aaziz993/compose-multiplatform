@file:JvmName("Exception_jvm")

package klib.data.type

public fun Exception.toJavaException(): java.lang.Exception = java.lang.Exception(message, cause)

public fun java.lang.Exception.toKotlinException(): Exception = Exception(message, cause)
