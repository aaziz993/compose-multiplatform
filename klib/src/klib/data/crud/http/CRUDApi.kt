package klib.data.crud.http

import klib.data.crud.http.model.HttpCrud
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.ReqBuilder
import io.ktor.client.request.*
import io.ktor.client.statement.*

internal interface CRUDApi {

    @Headers("Content-Type: application/json")
    @POST("transaction")
    suspend fun transaction(@ReqBuilder ext: HttpRequestBuilder.() -> Unit)

    @Headers("Content-Type: application/json")
    @PUT("insert")
    suspend fun insert(@Body crud: HttpCrud.Insert<*>)

    @Headers("Content-Type: application/json")
    @PUT("insertAndReturn")
    suspend fun insertAndReturn(@Body crud: HttpCrud.InsertAndReturn<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("update")
    suspend fun update(@Body crud: HttpCrud.Update<*>): List<Boolean>

    @Headers("Content-Type: application/json")
    @POST("updateUntyped")
    suspend fun update(@Body crud: HttpCrud.UpdateProjection): Long

    @Headers("Content-Type: application/json")
    @PUT("upsert")
    suspend fun upsert(@Body crud: HttpCrud.Upsert<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun find(@Body crud: HttpCrud.Find): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("delete")
    suspend fun delete(@Body crud: HttpCrud.Delete): Long

    @Headers("Content-Type: application/json")
    @POST("aggregate")
    suspend fun aggregate(@Body crud: HttpCrud.Aggregate): HttpStatement
}
