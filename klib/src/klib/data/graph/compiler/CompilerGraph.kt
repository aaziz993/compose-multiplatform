package klib.data.graph.compiler

import klib.data.crud.CoroutineCrudRepository
import klib.data.graph.Graph

public class CompilerGraph<VID : Comparable<VID>, EID : Comparable<EID>>(
    verticesRepository: CoroutineCrudRepository<CompilerVertex<VID, EID>>,
    edgesRepository: CoroutineCrudRepository<CompilerEdge<EID, VID>>
) : Graph<CompilerVertex<VID, EID>, VID, CompilerEdge<EID, VID>, EID>(
    verticesRepository,
    edgesRepository,
)
