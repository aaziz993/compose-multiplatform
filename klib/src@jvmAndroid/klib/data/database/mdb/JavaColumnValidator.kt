package klib.data.database.mdb

public class JavaColumnValidator(
    private val columnValidator: com.healthmarketscience.jackcess.util.ColumnValidator
) : ColumnValidator {
    override fun validate(column: Column, value: Any?): Any? =
        columnValidator.validate(column.column, value)
}

internal fun ColumnValidator.toColumnValidator() =
    com.healthmarketscience.jackcess.util.ColumnValidator { column, value ->
        validate(Column(column), value)
    }