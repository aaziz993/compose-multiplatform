package klib.data.database.mdb

import com.healthmarketscience.jackcess.impl.RowIdImpl
import klib.data.type.collections.biMapOf

public actual class RowId(public val rowId: RowIdImpl) : Comparable<RowId> {
    public actual constructor(pageNumber: Int, rowNumber: Int) : this(RowIdImpl(pageNumber, rowNumber))

    public actual val pageNumber: Int
        get() = rowId.pageNumber

    public actual val rowNumber: Int
        get() = rowId.rowNumber

    public actual val type: RowIdType
        get() = TYPE_MAP[rowId.type]!!

    actual override fun compareTo(other: RowId): Int = rowId.compareTo(other.rowId)

    public actual companion object {
        public actual val FIRST_PAGE_NUMBER: Int = RowIdImpl.FIRST_PAGE_NUMBER
        public actual val LAST_PAGE_NUMBER: Int = RowIdImpl.LAST_PAGE_NUMBER

        public actual val INVALID_ROW_NUMBER: Int = RowIdImpl.INVALID_ROW_NUMBER

        public actual val FIRST_ROW_ID: RowId = RowId(RowIdImpl.FIRST_ROW_ID)
        public actual val LAST_ROW_ID: RowId = RowId(RowIdImpl.LAST_ROW_ID)

        internal val TYPE_MAP = biMapOf(
            RowIdImpl.Type.ALWAYS_FIRST to RowIdType.ALWAYS_FIRST,
            RowIdImpl.Type.NORMAL to RowIdType.NORMAL,
            RowIdImpl.Type.ALWAYS_LAST to RowIdType.ALWAYS_LAST
        )
    }
}