package klib.data.location.locale.weblate

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.QueryName
import de.jensklingenberg.ktorfit.http.Url
import klib.data.location.locale.weblate.model.WeblateTranslationsResponse
import klib.data.location.locale.weblate.model.WeblateUnitsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public interface WeblateApi {

    @GET("translations")
    public suspend fun getTranslations(
        @QueryName format: String? = null,
        @QueryName page: Int,
    ): WeblateTranslationsResponse

    @GET
    public suspend fun getTranslations(@Url url: String = "translations"): WeblateTranslationsResponse

    @GET("units")
    public suspend fun getUnits(
        @QueryName format: String? = null,
        @QueryName page: Int,
    ): WeblateUnitsResponse

    @GET
    public suspend fun getUnits(@Url url: String = "units"): WeblateUnitsResponse
}

public fun WeblateApi.getAllTranslations(): Flow<WeblateTranslationsResponse> = flow {
    var url: String? = "translations"
    while (url != null) {
        val response = getTranslations(url)
        emit(response)
        url = response.next
    }
}

public fun WeblateApi.getAllUnits(): Flow<WeblateUnitsResponse> = flow {
    var url: String? = "units"
    while (url != null) {
        val response = getUnits(url)
        emit(response)
        url = response.next
    }
}
