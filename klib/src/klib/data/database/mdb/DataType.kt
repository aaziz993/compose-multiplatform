package klib.data.database.mdb


/**
 * Supported access data types.
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public enum class DataType {
    BOOLEAN,
    BYTE,
    INT,
    LONG,
    MONEY,
    FLOAT,
    DOUBLE,
    SHORT_DATE_TIME,
    BINARY,
    TEXT,
    OLE,
    MEMO,
    UNKNOWN_0D,
    GUID,
    NUMERIC,


    UNKNOWN_11,


    COMPLEX_TYPE,


    BIG_INT,


    EXT_DATE_TIME,


    UNSUPPORTED_FIXED_LEN,


    UNSUPPORTED_VAR_LEN;
}