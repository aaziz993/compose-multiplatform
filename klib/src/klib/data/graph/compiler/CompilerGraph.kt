package klib.data.graph.compiler

import klib.data.crud.CoroutineCRUDRepository
import klib.data.graph.Graph

public class CompilerGraph<VID : Comparable<VID>, EID : Comparable<EID>>(
    verticesRepository: CoroutineCRUDRepository<CompilerVertex<VID, EID>>,
    edgesRepository: CoroutineCRUDRepository<CompilerEdge<EID, VID>>
) : Graph<CompilerVertex<VID, EID>, VID, CompilerEdge<EID, VID>, EID>(
    verticesRepository,
    edgesRepository,
)
