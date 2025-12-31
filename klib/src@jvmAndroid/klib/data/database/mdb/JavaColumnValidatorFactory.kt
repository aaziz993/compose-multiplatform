package klib.data.database.mdb

public class JavaColumnValidatorFactory(
    private val columnValidatorFactory: com.healthmarketscience.jackcess.util.ColumnValidatorFactory
) : ColumnValidatorFactory {
    override fun createValidator(column: Column): ColumnValidator? =
        JavaColumnValidator(
            columnValidatorFactory.createValidator(column.column)
        )
}

internal fun ColumnValidatorFactory.toColumnValidatorFactory() =
    com.healthmarketscience.jackcess.util.ColumnValidatorFactory { column ->
        createValidator(Column(column))?.toColumnValidator()
    }
