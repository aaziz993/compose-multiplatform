package gradle.collection

import klib.data.type.act

public infix fun <E> MutableCollection<E>.tryAddAll(value: Iterable<E>?): Boolean? =
    value?.let(::addAll)

public infix fun <E> MutableCollection<E>.trySet(value: Iterable<E>?): Boolean? =
    tryAddAll(value?.act(::clear))
