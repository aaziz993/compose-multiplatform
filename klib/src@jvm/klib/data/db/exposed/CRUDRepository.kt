package klib.data.db.exposed

import klib.data.AggregateExpression
import klib.data.And
import klib.data.Avg
import klib.data.Between
import klib.data.BooleanValue
import klib.data.BooleanVariable
import klib.data.Count
import klib.data.Equals
import klib.data.EqualsPattern
import klib.data.Field
import klib.data.GreaterEqualThan
import klib.data.GreaterThan
import klib.data.In
import klib.data.LessEqualThan
import klib.data.LessThan
import klib.data.Max
import klib.data.Min
import klib.data.Not
import klib.data.NotEquals
import klib.data.NotIn
import klib.data.Or
import klib.data.Projection
import klib.data.Sum
import klib.data.Value
import klib.data.Variable
import klib.data.crud.AbstractCRUDRepository
import klib.data.crud.CRUDRepository
import klib.data.crud.model.query.LimitOffset
import klib.data.crud.model.query.Order
import klib.data.transaction.Transaction
import klib.data.transaction.model.TransactionIsolation
import klib.data.transaction.model.r2dbcTransactionIsolation
import klib.data.type.collections.list.asList
import klib.data.type.reflection.memberProperty
import klib.data.type.tuples.to
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.datetime.TimeZone
import kotlinx.serialization.KSerializer
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.alias
import org.jetbrains.exposed.v1.core.avg
import org.jetbrains.exposed.v1.core.compoundAnd
import org.jetbrains.exposed.v1.core.compoundOr
import org.jetbrains.exposed.v1.core.count
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.greaterEq
import org.jetbrains.exposed.v1.core.less
import org.jetbrains.exposed.v1.core.lessEq
import org.jetbrains.exposed.v1.core.max
import org.jetbrains.exposed.v1.core.min
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.core.sum
import org.jetbrains.exposed.v1.r2dbc.Query
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.batchInsert
import org.jetbrains.exposed.v1.r2dbc.batchUpsert
import org.jetbrains.exposed.v1.r2dbc.deleteAll
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.select
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.transactions.transactionManager
import org.jetbrains.exposed.v1.r2dbc.update

public class CRUDRepository<T : Any>(
    private val database: R2dbcDatabase,
    private val table: Table,
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
) : AbstractCRUDRepository<T>() {

    private val createdAtColumn = createdAtProperty?.let(table::get)
    private val updatedAtColumn = updatedAtProperty?.let(table::get)
    private val onUpsertExclude = listOfNotNull(createdAtColumn)

    override fun createdAtNow(): Any = createdAtColumn!!.now(timeZone)

    override fun updatedAtNow(): Any = updatedAtColumn!!.now(timeZone)

    private fun T.withAtProperties(): Map<String, Any?> =
        toProperties().withCreatedAtProperties().withUpdatedAtProperties()

    // CRUD operations in Exposed must be called from within a transaction.
    // By default, a nested transaction block shares the transaction resources of its parent transaction block, so any effect on the child affects the parent
    // Since Exposed 0.16.1 it is possible to use nested transactions as separate transactions by setting useNestedTransactions = true on the desired Database instance.
    override suspend fun <R> transactional(block: suspend CRUDRepository<T>.(Transaction) -> R): R =
        suspendTransaction(
            transactionIsolation?.r2dbcTransactionIsolation
                ?: database.transactionManager.defaultIsolationLevel
                ?: error("A default isolation level for this transaction has not been set"),
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
            block(R2dbcTransaction(this))
        }

    override suspend fun insert(entities: List<T>): Unit = tryTransactional {
        table.batchInsert(entities.withCreatedAtProperties()) { entity -> set(entity) }
    }

    override suspend fun insertAndReturn(entities: List<T>): List<T> = tryTransactional {
        table.batchInsert(entities.withCreatedAtProperties()) { entity -> set(entity) }.map(::toEntity)
    }

    override suspend fun update(entities: List<T>): List<Boolean> = tryTransactional {
        entities.map { entity -> table.update { statement -> statement.set(entity.withUpdatedAtProperties()) } > 0 }
    }

    override suspend fun update(properties: List<Map<String, Any?>>, predicate: BooleanVariable?): Long =
        tryTransactional {
            (if (predicate == null) properties.map { entity -> table.update { statement -> statement.set(entity) } }
            else properties.map { entity ->
                table.update({ predicate.toPredicate() }) { statement -> statement.set(entity) }
            }).first().let(Int::toLong)
        }

    override suspend fun upsert(entities: List<T>): List<T> = tryTransactional {
        table.batchUpsert(entities, onUpdateExclude = onUpsertExclude) { entity ->
            set(entity.withAtProperties())
        }.map(::toEntity)
    }

    override fun find(sort: List<Order>?, predicate: BooleanVariable?, limitOffset: LimitOffset?): Flow<T> = flow {
        tryTransactional {
            table.selectAll().find(sort, predicate, limitOffset).collect { row -> emit(toEntity(row)) }
        }
    }

    override fun find(
        projections: List<Variable>,
        sort: List<Order>?,
        predicate: BooleanVariable?,
        limitOffset: LimitOffset?
    ): Flow<List<Any?>> = flow {
        tryTransactional {
            val columns = projections.filterIsInstance<Projection>().map { projection ->
                table[projection.value]!!.let { column -> projection.alias?.let(column::alias) ?: column }
            }

            table.select(columns).find(sort, predicate, limitOffset).collect { row -> emit(row.toList(columns)) }
        }
    }

    override suspend fun delete(predicate: BooleanVariable?): Long = tryTransactional {
        (if (predicate == null) table.deleteAll() else table.deleteWhere { predicate.toPredicate() }).toLong()
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> aggregate(aggregate: AggregateExpression<T>, predicate: BooleanVariable?): T = tryTransactional {
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

    private suspend fun <R> tryTransactional(block: suspend () -> R): R =
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

    @Suppress("UNCHECKED_CAST")
    private fun BooleanVariable.toPredicate(): Op<Boolean> =
        this<Expression<*>>(
            { value ->
                when (value) {
                    is Field -> (table[value.value] as Column<Boolean>).eq(true)
                    is BooleanValue -> if (value.value) Op.TRUE else Op.FALSE

                    else -> error("Unsupported value: $value")
                }
            },
        )
        { expression, arguments ->
            when (expression) {
                is And -> arguments.asList<Op<Boolean>>().compoundAnd()
                is Or -> arguments.asList<Op<Boolean>>().compoundOr()
                is Not -> {
                    (table["some"] as ExpressionWithColumnType<Boolean>)
                    (arguments[0] as Expression<Boolean>)
                }

                is Equals -> arguments[0].eq(arguments[1])
                is NotEquals -> arguments[0].neq(arguments[1])
                is GreaterThan -> arguments[0].greater(arguments[1])
                is GreaterEqualThan -> arguments[0].greaterEq(arguments[1])
                is LessThan -> arguments[0].less(arguments[1])
                is LessEqualThan -> arguments[0].lessEq(arguments[1])
//                is EqualsPattern ->

                else -> error("Unsupported expression: $expression")
            }
        } as Op<Boolean>

    @Suppress("UNCHECKED_CAST")
    private fun Any.eval(expression: Expression, arguments: List<Any?>): Any {

        var funArgs = arguments

        val funName = when (expression) {
            is And -> {}

            is Or -> {}

            is Equals -> {
                if (expression.arguments.size > 2) {
                    throw IllegalArgumentException("Unsupported \"${this::class.simpleName}\" with match all or ignore case options")
                }
                "eq"
            }

            is NotEquals -> "neq"

            is EqualsPattern -> {
                val matchAll = (expression.arguments[3] as BooleanValue).value

                if (matchAll) {
                    throw IllegalArgumentException("Unsupported \"${this::class.simpleName}\" with match all option")
                }

                funArgs = arguments.dropLast(1)

                "regexp"
            }

            is Between -> "between"

            is GreaterThan -> "grater"

            is GreaterEqualThan -> "greaterEq"

            is LessThan -> "less"

            is LessEqualThan -> "lessEq"

            is In -> "inList"

            is NotIn -> "notInList"

            else -> throw IllegalArgumentException("Unsupported expression type \"${this::class.simpleName}\"")
        }

        val columnValueKType: KType? = null

        arguments.map { arg ->
            when (arg) {
                is Field ->
                    table[arg.value]!!.to { column -> column::class.starProjectedType }

                is Value<*> -> arg.value to arg::class.memberProperty("value")!!.returnType
                else -> arg to (arg?.let { it::class.starProjectedType } ?: columnValueKType)!!
            }
        }
    }
}

