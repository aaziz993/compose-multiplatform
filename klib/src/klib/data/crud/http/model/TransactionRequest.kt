package klib.data.crud.http.model

import kotlinx.serialization.Serializable

@Serializable
public data class TransactionRequest(val transactionId: String)
