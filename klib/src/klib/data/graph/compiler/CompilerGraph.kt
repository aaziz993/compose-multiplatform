package klib.data.graph.compiler

import klib.data.crud.CRUDRepository
import klib.data.graph.Graph

public class CompilerGraph<VID : Comparable<VID>, EID : Comparable<EID>>(
    verticesRepository: CRUDRepository<CompilerVertex<VID, EID>>,
    edgesRepository: CRUDRepository<CompilerEdge<EID, VID>>
) : Graph<CompilerVertex<VID, EID>, VID, CompilerEdge<EID, VID>, EID>(
    verticesRepository,
    edgesRepository,
)
