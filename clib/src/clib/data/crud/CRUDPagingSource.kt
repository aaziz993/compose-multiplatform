package clib.data.crud

import clib.data.paging.AbstractPagingSource
import klib.data.crud.model.query.LimitOffset

public class CRUDPagingSource<T : Any>(
    private val fetchData: suspend (limitOffset: LimitOffset) -> List<T>,
    public val firstItemOffset: Long = 0,
    disablePrepend: Boolean = false,
) : AbstractPagingSource<Long, T>(disablePrepend) {

    override suspend fun fetchData(loadKey: Long?, pageSize: Int): List<T> =
        fetchData(LimitOffset((loadKey ?: 0) * pageSize.toLong() + firstItemOffset, pageSize.toLong()))

    override fun getPrevKey(loadKey: Long): Long = loadKey.dec()

    override fun getNextKey(loadKey: Long?): Long? = loadKey?.inc() ?: 1
}
