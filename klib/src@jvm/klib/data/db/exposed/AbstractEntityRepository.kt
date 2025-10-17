package klib.data.db.exposed

import klib.data.crud.AbstractEntityRepository
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

public abstract class AbstractEntityRepository<T : Any> : AbstractEntityRepository<T>() {

    protected abstract val table: Table

    private val createdAtColumn = createdAtProperty?.let(table::get)
    private val updatedAtColumn = updatedAtProperty?.let(table::get)

    protected val onUpsertExclude: List<Column<*>> = listOfNotNull(createdAtColumn)

    override fun createdAtNow(): Any = createdAtColumn!!.now(timeZone)

    override fun updatedAtNow(): Any = updatedAtColumn!!.now(timeZone)

    protected fun T.withAtProperties(): Map<String, Any?> =
        toProperties().withCreatedAtProperties().withUpdatedAtProperties()
}
