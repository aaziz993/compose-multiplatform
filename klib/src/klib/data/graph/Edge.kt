package klib.data.graph

import klib.data.crud.CoroutineCrudRepository
import klib.data.query.BooleanOperand
import klib.data.query.f
import kotlinx.coroutines.flow.firstOrNull

public abstract class Edge<E : Edge<E, ID, V, VID>, ID : Comparable<ID>, V : Vertex<V, VID, E, ID>, VID : Comparable<VID>>(
    id: ID?,
    public val fromVertexId: VID,
    public val toVertexId: VID,
) : GraphEntity<E, ID>(id) {

    public suspend fun CoroutineCrudRepository<V>.fromVertex(predicate: BooleanOperand? = null): V? = find(
        predicate = "id".f.eq(fromVertexId).let {
            if (predicate == null) {
                it
            }
            else {
                it.and(predicate)
            }
        },
    ).firstOrNull()

    public suspend fun Graph<V, VID, E, ID>.fromVertex(predicate: BooleanOperand? = null): V? = verticesRepository.fromVertex(predicate)

    public suspend fun CoroutineCrudRepository<V>.toVertex(predicate: BooleanOperand? = null): V? = find(
        predicate = "id".f.eq(toVertexId).let {
            if (predicate == null) {
                it
            }
            else {
                it.and(predicate)
            }
        },
    ).firstOrNull()

    public suspend fun Graph<V, VID, E, ID>.toVertex(predicate: BooleanOperand? = null): V? = verticesRepository.toVertex(predicate)
}
