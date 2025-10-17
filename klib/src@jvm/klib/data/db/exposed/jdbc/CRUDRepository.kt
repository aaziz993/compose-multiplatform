package klib.data.db.exposed.jdbc

import com.ionspin.kotlin.bignum.decimal.toJavaBigDecimal
import klib.data.AggregateExpression
import klib.data.And
import klib.data.Avg
import klib.data.Between
import klib.data.BigDecimalValue
import klib.data.BooleanCollection
import klib.data.BooleanValue
import klib.data.BooleanVariable
import klib.data.ByteValue
import klib.data.CharValue
import klib.data.Count
import klib.data.DoubleValue
import klib.data.DurationValue
import klib.data.Equals
import klib.data.EqualsPattern
import klib.data.Field
import klib.data.FloatValue
import klib.data.GreaterEqualThan
import klib.data.GreaterThan
import klib.data.In
import klib.data.IntValue
import klib.data.LessEqualThan
import klib.data.LessThan
import klib.data.Like
import klib.data.LocalDateTimeValue
import klib.data.LocalDateValue
import klib.data.LocalTimeValue
import klib.data.LongValue
import klib.data.Max
import klib.data.Min
import klib.data.Not
import klib.data.NotEquals
import klib.data.Or
import klib.data.Projection
import klib.data.ShortValue
import klib.data.StringValue
import klib.data.Sum
import klib.data.UByteValue
import klib.data.UIntValue
import klib.data.ULongValue
import klib.data.UShortValue
import klib.data.UuidValue
import klib.data.Variable
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.transaction.model.TransactionIsolation
import klib.data.type.collections.list.asList
import kotlinx.datetime.TimeZone
import kotlinx.serialization.KSerializer
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.LiteralOp
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.arrayLiteral
import org.jetbrains.exposed.v1.core.avg
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.booleanLiteral
import org.jetbrains.exposed.v1.core.byteLiteral
import org.jetbrains.exposed.v1.core.compoundAnd
import org.jetbrains.exposed.v1.core.compoundOr
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.decimalLiteral
import org.jetbrains.exposed.v1.core.doubleLiteral
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.floatLiteral
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.intLiteral
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.core.longLiteral
import org.jetbrains.exposed.v1.core.max
import org.jetbrains.exposed.v1.core.min
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.not
import org.jetbrains.exposed.v1.core.regexp
import org.jetbrains.exposed.v1.core.shortLiteral
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.stringLiteral
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.core.ubyteLiteral
import org.jetbrains.exposed.v1.core.uintLiteral
import org.jetbrains.exposed.v1.core.ulongLiteral
import org.jetbrains.exposed.v1.core.ushortLiteral
import org.jetbrains.exposed.v1.datetime.dateLiteral
import org.jetbrains.exposed.v1.datetime.dateTimeLiteral
import org.jetbrains.exposed.v1.datetime.durationLiteral
import org.jetbrains.exposed.v1.datetime.timeLiteral
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.batchUpsert
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transactionManager
import org.jetbrains.exposed.v1.jdbc.update
import klib.data.crud.CRUDRepository
import klib.data.db.exposed.AbstractEntityRepository
import klib.data.db.exposed.get
import klib.data.db.exposed.toList
import klib.data.db.exposed.toMap
import klib.data.transaction.model.javaSqlTransactionIsolation
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

public class CRUDRepository<T : Any>(
    private val database: Database,
    override val table: Table,
    override val kSerializer: KSerializer<T>,
    override val createdAtProperty: String? = "createdAt",
    override val updatedAtProperty: String? = "updatedAt",
    override val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    public val transactionIsolation: TransactionIsolation? = null,
    public val readOnly: Boolean = false,
    public val statementCount: Int? = null,
    public val duration: Long? = null,
    public val warnLongQueriesDuration: Long? = null,
    public val debug: Boolean? = null,
    public val maxAttempts: Int? = null,
    public val minRetryDelay: Long? = null,
    public val maxRetryDelay: Long? = null,
    public val queryTimeout: Int? = null,
) : CRUDRepository<T>, AbstractEntityRepository<T>() {
    // CRUD operations in Exposed must be called from within a transaction.
    // By default, a nested transaction block shares the transaction resources of its parent transaction block, so any effect on the child affects the parent
    // Since Exposed 0.16.1 it is possible to use nested transactions as separate transactions by setting useNestedTransactions = true on the desired Database instance.
    override fun <R> transactional(block: CRUDRepository<T>.(klib.data.transaction.CoroutineTransaction) -> R): R =
        transaction(
            transactionIsolation?.javaSqlTransactionIsolation
                ?: database.transactionManager.defaultIsolationLevel,
            readOnly,
            database,
        ) {
            this@CRUDRepository.statementCount?.let { statementCount = it }
            this@CRUDRepository.duration?.let { duration = it }
            this@CRUDRepository.warnLongQueriesDuration?.let { warnLongQueriesDuration = it }
            this@CRUDRepository.debug?.let { debug = it }
            this@CRUDRepository.maxAttempts?.let { maxAttempts = it }
            this@CRUDRepository.minRetryDelay?.let { minRetryDelay = it }
            this@CRUDRepository.maxRetryDelay?.let { maxRetryDelay = it }
            this@CRUDRepository.queryTimeout?.let { queryTimeout = it }
            block(Transaction(this))
        }

    override fun insert(entities: List<T>): Unit = tryTransactional {
        table.batchInsert(entities.withCreatedAtProperties()) { entity -> set(entity) }
    }

    override fun insertAndReturn(entities: List<T>): List<T> = tryTransactional {
        table.batchInsert(entities.withCreatedAtProperties()) { entity -> set(entity) }.map(::toEntity)
    }

    override fun update(entities: List<T>): List<Boolean> = tryTransactional {
        entities.map { entity -> table.update { statement -> statement.set(entity.withUpdatedAtProperties()) } > 0 }
    }

    override fun update(properties: List<Map<String, Any?>>, predicate: BooleanVariable?): Long =
        tryTransactional {
            (if (predicate == null) properties.map { entity -> table.update { statement -> statement.set(entity) } }
            else properties.map { entity ->
                table.update({ predicate.toPredicate() }) { statement -> statement.set(entity) }
            }).first().let(Int::toLong)
        }

    override fun upsert(entities: List<T>): List<T> = tryTransactional {
        table.batchUpsert(entities, onUpdateExclude = onUpsertExclude) { entity ->
            set(entity.withAtProperties())
        }.map(::toEntity)
    }

    override fun find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?): Iterable<T> =
        tryTransactional {
            table.selectAll().find(sort, predicate, limitOffset).map(::toEntity)
        }

    override fun find(
        projections: List<Variable>,
        sort: List<Order>?,
        predicate: BooleanVariable?,
        limitOffset: LimitOffset?
    ): Iterable<List<Any?>> =
        tryTransactional {
            val columns = projections.filterIsInstance<Projection>().map { projection ->
                table[projection.value]!!.let { column -> projection.alias?.let(column::alias) ?: column }
            }

            table.select(columns).find(sort, predicate, limitOffset).map { row -> row.toList(columns) }
        }

    override fun delete(predicate: BooleanVariable?): Long = tryTransactional {
        (if (predicate == null) table.deleteAll() else table.deleteWhere { predicate.toPredicate() }).toLong()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> aggregate(aggregate: AggregateExpression<T>, predicate: BooleanVariable?): T = tryTransactional {
        val column: Column<Comparable<Any>>? = aggregate.projection?.let { projection ->
            table[projection.value]!!
        } as Column<Comparable<Any>>?

        if (column == null) {
            require(aggregate is Count) { "Invalid aggregation without column: $aggregate" }
            return@tryTransactional table.selectAll().predicate(predicate).count() as T
        }

        val distinct = aggregate.projection!!.distinct
        when (aggregate) {
            is Count -> table.select(column.count())
            is Max -> table.select(column.max())
            is Min -> table.select(column.min())
            is Avg -> table.select(column.avg())
            is Sum -> table.select(column.sum())
        }.withDistinct(distinct).predicate(predicate).singleOrNull() as T
    }

    private fun toEntity(row: ResultRow): T = row.toMap(table).toEntity()

    private fun <R> tryTransactional(block: () -> R): R =
        if (database.transactionManager.currentOrNull() == null) transactional { block() } else block()

    private fun Query.find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?) =
        predicate(predicate).apply {
            sort?.forEach { order ->
                orderBy(
                    table[order.name]!!,
                    if (order.ascending)
                        when (order.nullFirst) {
                            true -> SortOrder.ASC_NULLS_FIRST
                            false -> SortOrder.ASC_NULLS_LAST
                            else -> SortOrder.ASC
                        }
                    else when (order.nullFirst) {
                        true -> SortOrder.DESC_NULLS_FIRST
                        false -> SortOrder.DESC_NULLS_LAST
                        else -> SortOrder.DESC
                    },
                )
            }

            limitOffset?.offset?.let(::offset)
            limitOffset?.limit?.toInt()?.let(::limit)
        }

    @Suppress("UNCHECKED_CAST")
    private fun <T> UpdateBuilder<T>.set(map: Map<String, Any?>) = map.forEach { (property, value) ->
        this[table[property] as Column<Any?>] = value
    }

    private fun Query.predicate(predicate: BooleanVariable?): Query = apply {
        if (predicate != null) where(predicate.toPredicate())
    }

    private fun BooleanVariable.toPredicate(): Op<Boolean> =
        this<Expression<*>, Op<Boolean>>(
            { value ->
                when (value) {
                    is Field -> table[value.value]!!
                    is BooleanValue -> booleanLiteral(value.value)
                    is BooleanCollection -> arrayLiteral<Boolean>(value.value)
                    is UByteValue -> ubyteLiteral(value.value)
                    is UShortValue -> ushortLiteral(value.value)
                    is UIntValue -> uintLiteral(value.value)
                    is ULongValue -> ulongLiteral(value.value)
                    is ByteValue -> byteLiteral(value.value)
                    is ShortValue -> shortLiteral(value.value)
                    is IntValue -> intLiteral(value.value)
                    is LongValue -> longLiteral(value.value)
                    is FloatValue -> floatLiteral(value.value)
                    is DoubleValue -> doubleLiteral(value.value)
                    is CharValue -> stringLiteral(value.value.toString())
                    is StringValue -> stringLiteral(value.value)
                    is BigDecimalValue -> decimalLiteral(value.value.toJavaBigDecimal())
                    is LocalTimeValue -> timeLiteral(value.value)
                    is LocalDateValue -> dateLiteral(value.value)
                    is LocalDateTimeValue -> dateTimeLiteral(value.value)
                    is DurationValue -> durationLiteral(value.value)
                    is UuidValue -> stringLiteral(value.value.toString())

                    else -> error("")
                }
            },
        )
        { expression, arguments ->
            when (expression) {
                is And -> arguments.asList<Op<Boolean>>().compoundAnd()
                is Or -> arguments.asList<Op<Boolean>>().compoundOr()
                is Not -> not(arguments[0] as Expression<Boolean>)
                is Equals -> arguments[0].eq(arguments[1])
                is NotEquals -> arguments[0].neq(arguments[1])
                is EqualsPattern -> (arguments[0] as Expression<String>).regexp(
                    arguments[1] as Expression<String>,
                    (arguments.getOrNull(2) as LiteralOp<Boolean>?)?.value?.not() ?: true,
                )

                is Like -> (arguments[0] as Expression<String>).like(arguments[1] as ExpressionWithColumnType<String>)
                is GreaterThan -> arguments[0].greater(arguments[1])
                is GreaterEqualThan -> arguments[0].greaterEq(arguments[1])
                is LessThan -> arguments[0].less(arguments[1])
                is LessEqualThan -> arguments[0].lessEq(arguments[1])
                is Between -> arguments[0].between(arguments[1], arguments[2])
                is In -> (arguments[0] as ExpressionWithColumnType<String>).inList((arguments[1] as LiteralOp<List<*>>).value)

                else -> error("Unsupported expression: $expression")
            }
        }
}

