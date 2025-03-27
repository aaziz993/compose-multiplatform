package gradle

public fun Boolean.takeIfTrue(): Boolean? = takeIfTrue()

public fun Boolean.ifTrue(action: () -> Unit): Boolean? =
    if (this) act(action) else null

public fun Boolean.ifFalse(action: () -> Unit): Boolean? =
    (!this).ifTrue(action)
