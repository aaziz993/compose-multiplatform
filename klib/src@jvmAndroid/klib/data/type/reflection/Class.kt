package klib.data.type.reflection

import kotlin.reflect.KClass


public fun String.toClass(): Class<*> = Class<*>.forName(this)

public fun String.toKClass(): KClass<*> = toClass().kotlin

@Suppress("UNCHECKED_CAST")
public fun String.toObjectInstance(): Any? = toClass().kotlin.objectInstance
