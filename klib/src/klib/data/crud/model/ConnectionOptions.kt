package klib.data.crud.model

import klib.data.type.auth.AuthResource
import kotlinx.datetime.TimeZone

public interface ConnectionOptions {

    public val transactionIsolation: Int?
    public val statementCount: Int?
    public val duration: Long?
    public val warnLongQueriesDuration: Long?
    public val debug: Boolean?
    public val maxAttempts: Int?
    public val minRetryDelay: Long?
    public val maxRetryDelay: Long?
    public val queryTimeout: Int?
    public val timeZone: TimeZone
    public val readAuth: AuthResource?
    public val writeAuth: AuthResource?
}
