package clib.presentation.components.lazycolumn.crud.model.config

import clib.presentation.components.lazycolumn.crud.model.CRUDLazyColumnStateData
import klib.data.auth.AuthResource
import kotlinx.serialization.Serializable

@Serializable
public data class CRUDLazyColumnConfig(
    val readAuth: AuthResource? = null,
    val writeAuth: AuthResource? = readAuth,
    val isReadOnly: Boolean = false,
    val isMultiSort: Boolean = true,
    val showPageCount: Int = 10,
    val state: CRUDLazyColumnStateData = CRUDLazyColumnStateData()
)
