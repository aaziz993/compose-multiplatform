package klib.data.query

import kotlinx.serialization.Serializable

@Serializable
public data class Order(
    public val variable: Variable,
    public val ascending: Boolean = true,
    public val nullFirst: Boolean? = null,
)
