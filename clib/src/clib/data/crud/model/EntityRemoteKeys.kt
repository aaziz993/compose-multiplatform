package clib.data.crud.model

import clib.data.paging.model.RemoteKeys

public interface EntityRemoteKeys<ID : Any> : RemoteKeys<Long> {

    public val entityId: ID
}
