package klib.data.graph

import klib.data.crud.CoroutineCrudRepository

public open class Graph<V : Vertex<V, VID, E, EID>, VID : Comparable<VID>, E : Edge<E, EID, V, VID>, EID : Comparable<EID>>(
    public val verticesRepository: CoroutineCrudRepository<V>,
    public val edgesRepository: CoroutineCrudRepository<E>
)
