package klib.data.db.crud.model.query

import kotlinx.serialization.Serializable

@Serializable
public data class LimitOffset(
    public val offset: Long = 0,
    public val limit: Long = 1,
) {

    public fun getPage(firstItemOffset: Int = 0): Long = (offset - firstItemOffset) / limit
}
