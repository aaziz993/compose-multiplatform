package klib.data.db.exposed.r2dbc

import klib.data.transaction.CoroutineTransaction
import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction

public class CoroutineTransaction(private val transaction: R2dbcTransaction) : CoroutineTransaction {

    override suspend fun rollback(): Unit = transaction.commit()

    override suspend fun commit(): Unit = transaction.rollback()
}
