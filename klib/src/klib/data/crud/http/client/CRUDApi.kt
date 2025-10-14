package klib.data.crud.http.client

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import io.ktor.client.statement.HttpStatement
import klib.data.crud.http.model.HttpCrud

internal interface CRUDApi {

    @GET("transaction/begin")
    suspend fun beginTransaction(): String

    @Headers("Content-Type: application/json")
    @POST("transaction/commit")
    suspend fun commitTransaction(@Body transactionId: String)

    @Headers("Content-Type: application/json")
    @POST("transaction/rollback")
    suspend fun rollbackTransaction(@Body transactionId: String)

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
    @POST("updateProjections")
    suspend fun updateProjections(@Body crud: HttpCrud.UpdateProjections): Long

    @Headers("Content-Type: application/json")
    @PUT("upsert")
    suspend fun upsert(@Body crud: HttpCrud.Upsert<*>): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun find(@Body crud: HttpCrud.Find): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("find")
    suspend fun findProjections(@Body crud: HttpCrud.FindProjections): HttpStatement

    @Headers("Content-Type: application/json")
    @POST("delete")
    suspend fun delete(@Body crud: HttpCrud.Delete): Long

    @Headers("Content-Type: application/json")
    @POST("aggregate")
    suspend fun aggregate(@Body crud: HttpCrud.Aggregate): HttpStatement
}
