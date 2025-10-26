package klib.data.query

import kotlinx.serialization.Serializable

@Serializable
public data class LimitOffset(
    public val offset: Long? = null,
    public val limit: Long? = null,
) {

    public fun getPage(firstItemOffset: Int = 0): Long =
        if (offset == null || limit == null) 0L else (offset - firstItemOffset) / limit
}
