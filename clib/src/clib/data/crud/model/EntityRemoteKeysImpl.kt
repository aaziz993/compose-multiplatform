package clib.data.crud.model

public data class EntityRemoteKeysImpl<ID : Any>(
    override val entityId: ID,
    override val prevKey: Long?,
    override val currentKey: Long?,
    override val nextKey: Long?
) : EntityRemoteKeys<ID>
