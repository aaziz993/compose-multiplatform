package klib.data.type.reflection

import kotlin.reflect.KCallable

public operator fun KCallable<*>.invoke(vararg args: Any?): Any? = call(*args)
