package klib.data.type.primitive

import klib.data.type.act

public fun Boolean.takeIfTrue(): Boolean? = takeIfTrue()

public infix fun Boolean.ifTrue(action: () -> Unit): Boolean? =
    if (this) act(action) else null

public infix fun Boolean.ifFalse(action: () -> Unit): Boolean? =
    (!this) ifTrue action
