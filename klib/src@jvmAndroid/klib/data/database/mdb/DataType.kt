package klib.data.database.mdb

import klib.data.type.collections.bimap.biMapOf

internal val DATA_TYPE_MAP = biMapOf(
    com.healthmarketscience.jackcess.DataType.BOOLEAN to DataType.BOOLEAN,
    com.healthmarketscience.jackcess.DataType.BYTE to DataType.BYTE,
    com.healthmarketscience.jackcess.DataType.INT to DataType.INT,
    com.healthmarketscience.jackcess.DataType.LONG to DataType.LONG,
    com.healthmarketscience.jackcess.DataType.MONEY to DataType.MONEY,
    com.healthmarketscience.jackcess.DataType.FLOAT to DataType.FLOAT,
    com.healthmarketscience.jackcess.DataType.DOUBLE to DataType.DOUBLE,
    com.healthmarketscience.jackcess.DataType.SHORT_DATE_TIME to DataType.SHORT_DATE_TIME,
    com.healthmarketscience.jackcess.DataType.BINARY to DataType.BINARY,
    com.healthmarketscience.jackcess.DataType.TEXT to DataType.TEXT,
    com.healthmarketscience.jackcess.DataType.OLE to DataType.OLE,
    com.healthmarketscience.jackcess.DataType.MEMO to DataType.MEMO,
    com.healthmarketscience.jackcess.DataType.UNKNOWN_0D to DataType.UNKNOWN_0D,
    com.healthmarketscience.jackcess.DataType.GUID to DataType.GUID,
    com.healthmarketscience.jackcess.DataType.NUMERIC to DataType.NUMERIC,
    com.healthmarketscience.jackcess.DataType.UNKNOWN_11 to DataType.UNKNOWN_11,
    com.healthmarketscience.jackcess.DataType.COMPLEX_TYPE to DataType.COMPLEX_TYPE,
    com.healthmarketscience.jackcess.DataType.BIG_INT to DataType.BIG_INT,
    com.healthmarketscience.jackcess.DataType.EXT_DATE_TIME to DataType.EXT_DATE_TIME,
    com.healthmarketscience.jackcess.DataType.UNSUPPORTED_FIXEDLEN to DataType.UNSUPPORTED_FIXED_LEN,
    com.healthmarketscience.jackcess.DataType.UNSUPPORTED_VARLEN to DataType.UNSUPPORTED_VAR_LEN,
)
