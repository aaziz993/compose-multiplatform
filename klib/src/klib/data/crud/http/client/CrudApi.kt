package klib.data.crud.http.client

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import io.ktor.client.statement.HttpStatement
import klib.data.crud.http.model.HttpCrud

internal interface CrudApi {

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun find(@Body crud: HttpCrud.Find): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("findProperties")
    suspend fun findProperties(@Body crud: HttpCrud.FindProperties): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("observe")
    suspend fun observe(@Body crud: HttpCrud.Observe): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("observeProperties")
    suspend fun observeProperties(@Body crud: HttpCrud.ObserveProperties): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("delete")
    suspend fun delete(@Body crud: HttpCrud.Delete): Long

    @Headers("Content-Type: application/json")
    @POST("aggregate")
    suspend fun aggregate(@Body crud: HttpCrud.Aggregate): HttpStatement

    @Headers("Content-Type: application/json")
    @PUT("insert")
    suspend fun insert(@Body crud: HttpCrud.Insert<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("update")
    suspend fun update(@Body crud: HttpCrud.Update): Long

    @Headers("Content-Type: application/json")
    @POST("updateEntity")
    suspend fun updateEntity(@Body crud: HttpCrud.UpdateEntity<*>): Boolean

    @Headers("Content-Type: application/json")
    @PUT("upsert")
    suspend fun upsert(@Body crud: HttpCrud.Upsert<*>): HttpStatement
}
