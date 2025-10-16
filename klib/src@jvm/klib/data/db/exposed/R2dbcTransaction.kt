package klib.data.db.exposed

import klib.data.transaction.Transaction
import org.jetbrains.exposed.v1.r2dbc.R2dbcTransaction

public class R2dbcTransaction(private val transaction: R2dbcTransaction) : Transaction {

    override suspend fun rollback(): Unit = transaction.commit()

    override suspend fun commit(): Unit = transaction.rollback()
}
