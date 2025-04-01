package klib.data.type.reflection

import klib.data.type.ifTrue
import kotlin.reflect.KFunction0

public infix fun <T> KFunction0<T>.trySet(value: Boolean?): Boolean? =
    value?.ifTrue(::invoke)
