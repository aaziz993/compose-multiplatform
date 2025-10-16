package klib.data.db.exposed.column

import org.jetbrains.exposed.v1.core.ColumnType
import org.jetbrains.exposed.v1.core.ComparisonOp
import org.jetbrains.exposed.v1.core.CustomFunction
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.statements.api.PreparedStatementApi
import org.jetbrains.exposed.v1.core.wrap
import org.postgresql.util.PGobject

public abstract class RangeColumnType<T : Comparable<T>, R : ClosedRange<T>>(
    public val subType: ColumnType<T>,
) : ColumnType<R>() {

    public abstract fun List<String>.toRange(): R

    override fun nonNullValueToString(value: R): String {
        return "[${value.start},${value.endInclusive}]"
    }

    override fun nonNullValueAsDefaultString(value: R): String {
        return "'${nonNullValueToString(value)}'"
    }

    @Suppress("UNCHECKED_CAST")
    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val parameterValue: PGobject? = value?.let {
            PGobject().apply {
                type = sqlType()
                this.value = nonNullValueToString(it as R)
            }
        }
        super.setParameter(stmt, index, parameterValue)
    }

    override fun valueFromDB(value: Any): R? = when (value) {
        is PGobject -> value.value?.let {
            val components = it.trim('[', ')').split(',')
            components.toRange()
        }

        else -> error("Retrieved unexpected value of type ${value::class.simpleName}")
    }
}

@Suppress("UNCHECKED_CAST")
public fun <T : Comparable<T>, CR : ClosedRange<T>, R : CR?> ExpressionWithColumnType<R>.upperBound() =
    CustomFunction("UPPER", (columnType as RangeColumnType<T, CR>).subType, this)

public infix fun <R : ClosedRange<*>?> ExpressionWithColumnType<R>.isContainedBy(other: R) =
    RangeIsContainedOp(this, wrap(other))

public class RangeIsContainedOp<R : ClosedRange<*>?>(
    left: Expression<R>,
    right: Expression<R>
) : ComparisonOp(left, right, "<@")

