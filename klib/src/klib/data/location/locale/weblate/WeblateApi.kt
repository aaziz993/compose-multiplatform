package klib.data.location.locale.weblate

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.QueryName
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import klib.data.location.locale.weblate.model.WeblateTranslationsResponse
import klib.data.location.locale.weblate.model.WeblateUnitsResponse
import klib.data.type.collections.pair
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

public interface WeblateApi {

    @GET("components/{project}/{component}/translations/")
    public suspend fun getTranslations(
        @Path project: String,
        @Path component: String,
        @QueryName format: String? = null,
        @QueryName page: Int? = null,
    ): WeblateTranslationsResponse

    @GET("translations/{project}/{component}/{language}/units/")
    public suspend fun getUnits(
        @Path project: String,
        @Path component: String,
        @Path language: String,
        @QueryName format: String? = null,
        @QueryName page: Int? = null,
    ): HttpResponse
}

public fun WeblateApi.getAllTranslations(
    project: String,
    component: String,
): Flow<WeblateTranslationsResponse> = flow {
    var url: String? = "translations/"
    while (url != null) {
        val params = url.parseParams()
        val response = getTranslations(project, component, params["format"], params["page"]?.toInt())
        emit(response)
        url = response.next
    }
}

public fun WeblateApi.getAllUnits(
    project: String,
    component: String,
    language: String,
): Flow<WeblateUnitsResponse> = flow {
    var url: String? = "units/"
    while (url != null) {
        val params = url.parseParams()

        url = getUnits(project, component, language, params["format"], params["page"]?.toInt())
            .takeIf { it.status == HttpStatusCode.OK }?.body<WeblateUnitsResponse>()?.let { response ->
                emit(response)
                response.next
            }
    }
}

private fun String.parseParams(): Map<String, String> {
    val params = substringAfter("?", "")

    if (params.isEmpty()) return emptyMap()

    return params.split("&")
        .associate { value ->
            value.split("=").pair()
        }
}
