package clib.ui.presentation.components.lazycolumn.crud.model.config

import clib.ui.presentation.components.lazycolumn.crud.model.CRUDLazyColumnStateData
import klib.data.db.crud.model.CRUDRepositoryConfig
import klib.data.type.auth.AuthResource
import kotlinx.datetime.TimeZone
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
