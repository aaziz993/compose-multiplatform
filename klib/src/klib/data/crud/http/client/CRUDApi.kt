package klib.data.crud.http.client

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.ReqBuilder
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpStatement
import klib.data.crud.http.model.HttpCrud

internal interface CRUDApi {

    @Headers("Content-Type: application/json")
    @PUT("insert")
    suspend fun insert(@Body crud: HttpCrud)

    @Headers("Content-Type: application/json")
    @PUT("insertAndReturn")
    suspend fun insertAndReturn(@Body crud: HttpCrud.InsertAndReturn<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("update")
    suspend fun update(@Body crud: HttpCrud.Update<*>): List<Boolean>

    @Headers("Content-Type: application/json")
    @POST("updateProjections")
    suspend fun updateProperties(@Body crud: HttpCrud.UpdateProperties): Long

    @Headers("Content-Type: application/json")
    @PUT("upsert")
    suspend fun upsert(@Body crud: HttpCrud.Upsert<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun find(@Body crud: HttpCrud.Find): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun findProperties(@Body crud: HttpCrud.FindProperties): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("delete")
    suspend fun delete(@Body crud: HttpCrud.Delete): Long

    @Headers("Content-Type: application/json")
    @POST("aggregate")
    suspend fun aggregate(@Body crud: HttpCrud.Aggregate): HttpStatement
}
