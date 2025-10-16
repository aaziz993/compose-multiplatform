package klib.data.db.exposed.mapper

import io.r2dbc.postgresql.codec.PostgresTypes
import io.r2dbc.postgresql.codec.PostgresqlObjectId
import io.r2dbc.spi.Parameters
import io.r2dbc.spi.Statement
import klib.data.db.exposed.column.IntRangeColumnType
import klib.data.db.exposed.column.r2dbc.CitextR2dbcColumnType
import klib.data.db.exposed.column.r2dbc.RangeR2dbcColumnType
import kotlin.reflect.KClass
import org.jetbrains.exposed.v1.core.IColumnType
import org.jetbrains.exposed.v1.core.vendors.DatabaseDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.r2dbc.mappers.R2dbcTypeMapping
import org.jetbrains.exposed.v1.r2dbc.mappers.TypeMapper

public class CustomTypeMapper : TypeMapper {

    override val priority: Double = 1.9

    override val dialects: List<KClass<PostgreSQLDialect>> = listOf(PostgreSQLDialect::class)

    override val columnTypes: List<KClass<out IColumnType<*>>> = listOf(
        CitextR2dbcColumnType::class,
        IntRangeColumnType::class,
    )

    override fun setValue(
        statement: Statement,
        dialect: DatabaseDialect,
        typeMapping: R2dbcTypeMapping,
        columnType: IColumnType<*>,
        value: Any?,
        index: Int
    ): Boolean {
        if (value == null) return false

        return when (columnType) {
            is CitextR2dbcColumnType -> {
                statement.bind(index - 1, Parameters.`in`(PostgresqlObjectId.UNSPECIFIED, value))
                true
            }

            is IntRangeColumnType -> {
                statement.bind(
                    index - 1,
                    Parameters.`in`(
                        PG_INT_RANGE_TYPE,
                        RangeR2dbcColumnType.toPostgresqlValue(value as IntRange),
                    ),
                )
                true
            }

            else -> false
        }
    }

    private val PG_INT_RANGE_TYPE = PostgresTypes.PostgresType(
        3904, 3904, 3905, 3905, "int4range", "R",
    )
}
