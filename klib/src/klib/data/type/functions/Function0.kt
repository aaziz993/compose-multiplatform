package klib.data.type.functions

import klib.data.type.primitives.ifTrue

public infix fun <T> Function0<T>.invokeIf(value: Boolean): Boolean? =
    value.ifTrue(::invoke)

public infix fun <T> Function0<T>.tryInvokeIf(value: Boolean?): Boolean? =
    value?.let(::invokeIf)