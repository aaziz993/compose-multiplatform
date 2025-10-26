package klib.data.graph

import klib.data.crud.CoroutineCrudRepository
import klib.data.entity.Entity
import klib.data.query.f

public abstract class GraphEntity<T : GraphEntity<T, ID>, ID : Comparable<ID>>(
    override val id: ID? = null
): Entity<ID> {

    @Suppress("UNCHECKED_CAST")
    public suspend fun CoroutineCrudRepository<T>.save(): T = insert(listOf(this as T)).first()

    public suspend fun CoroutineCrudRepository<T>.delete(): Boolean = delete("id".f eq id!!) > 0L

    override fun equals(other: Any?): Boolean =
        this === other || (other is GraphEntity<*, *> && this::class == other::class && id == other.id)

    override fun hashCode(): Int = id.hashCode()
}
