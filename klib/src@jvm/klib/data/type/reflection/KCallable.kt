package klib.data.type.reflection

import kotlin.reflect.KCallable
import kotlin.reflect.full.instanceParameter

public operator fun KCallable<*>.invoke(
    instance: Any? = null,
    vararg args: Any?,
): Any? = if (instanceParameter == null) call(*args)
else call(instance ?: error("Instance is not provided"), *args)
