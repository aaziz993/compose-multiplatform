package klib.data.db.mdb

import klib.data.type.toJavaException
import klib.data.type.toKotlinException

public class JavaErrorHandler(private val errorHandler: com.healthmarketscience.jackcess.util.ErrorHandler) :
    ErrorHandler {

    override fun handleRowError(
        column: Column,
        columnData: ByteArray,
        location: ErrorHandler.Location,
        error: Exception
    ): Any = errorHandler.handleRowError(
        column.column,
        columnData,
        (location as JavaLocation).location,
        error.toJavaException(),
    )

    public class JavaLocation(internal val location: com.healthmarketscience.jackcess.util.ErrorHandler.Location) :
        ErrorHandler.Location {

        override val table: Table = Table(location.table)

        override fun toString(): String = location.toString()
    }
}

internal fun ErrorHandler.toErrorHandler() =
    com.healthmarketscience.jackcess.util.ErrorHandler { column, columnData, location, error ->
        handleRowError(
            Column(column),
            columnData,
            JavaErrorHandler.JavaLocation(location),
            error.toKotlinException(),
        )
    }
