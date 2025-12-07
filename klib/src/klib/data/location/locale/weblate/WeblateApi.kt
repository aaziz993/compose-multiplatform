package klib.data.location.locale.weblate

import klib.data.location.locale.weblate.model.WeblateTranslationsResponse
import klib.data.location.locale.weblate.model.WeblateUnitsResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.QueryName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public interface WeblateApi {

    @GET("{path}")
    public suspend fun getTranslations(
        @Path("path") path: String = "api/translations",
        @QueryName format: String? = null,
        @QueryName page: Int? = null,
    ): WeblateTranslationsResponse

    public fun getTranslations(): Flow<WeblateTranslationsResponse> = flow {
        var path: String? = "api/translations"
        while (path != null) {
            emit(getTranslations(path).also { response -> path = response.next })
        }
    }

    @GET("{path}")
    public suspend fun getUnits(
        @Path("path") path: String = "api/units",
        @QueryName format: String? = null,
        @QueryName page: Int? = null,
    ): WeblateUnitsResponse

    public fun getUnits(): Flow<WeblateUnitsResponse> = flow {
        var path: String? = "api/units"
        while (path != null) {
            emit(getUnits(path).also { response -> path = response.next })
        }
    }
}
