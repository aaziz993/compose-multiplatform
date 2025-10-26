package klib.data.graph

import klib.data.crud.CoroutineCrudRepository
import klib.data.query.BooleanOperand
import klib.data.query.f
import kotlinx.coroutines.flow.Flow

public abstract class Vertex<V : Vertex<V, ID, E, EID>, ID : Comparable<ID>, E : Edge<E, EID, V, ID>, EID : Comparable<EID>>(
    id: ID? = null,
) : GraphEntity<V, ID>(id) {

    public suspend fun CoroutineCrudRepository<E>.fromEdges(predicate: BooleanOperand? = null): Flow<E> = find(
        predicate = "fromVertexId".f.eq(id).let {
            if (predicate == null) {
                it
            }
            else {
                it.and(predicate)
            }
        },
    )

    public suspend fun Graph<V, ID, E, EID>.fromEdges(predicate: BooleanOperand? = null): Flow<E> = edgesRepository.fromEdges(predicate)

    public suspend fun CoroutineCrudRepository<E>.toEdges(predicate: BooleanOperand? = null): Flow<E> = find(
        predicate = "toVertexId".f.eq(id).let {
            if (predicate == null) {
                it
            }
            else {
                it.and(predicate)
            }
        },
    )

    public suspend fun Graph<V, ID, E, EID>.toEdges(predicate: BooleanOperand? = null): Flow<E> = edgesRepository.toEdges(predicate)

    public suspend fun CoroutineCrudRepository<E>.edges(predicate: BooleanOperand? = null): Flow<E> = find(
        predicate = "fromVertexId".f.eq(id).or("toVertexId".f.eq(id)).let {
            if (predicate == null) {
                it
            }
            else {
                it.and(predicate)
            }
        },
    )

    public suspend fun Graph<V, ID, E, EID>.edges(predicate: BooleanOperand? = null): Flow<E> = edgesRepository.edges(predicate)
}
