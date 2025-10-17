package klib.data.graph

import klib.data.crud.CoroutineCRUDRepository

public open class Graph<V : Vertex<V, VID, E, EID>, VID : Comparable<VID>, E : Edge<E, EID, V, VID>, EID : Comparable<EID>>(
    public val verticesRepository: CoroutineCRUDRepository<V>,
    public val edgesRepository: CoroutineCRUDRepository<E>
)
