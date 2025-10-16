package klib.data.type.reflection

@Suppress("UNCHECKED_CAST")
public fun <T : Any> String.toObjectInstance(): T = Class<*>.forName(this).kotlin.objectInstance as T
