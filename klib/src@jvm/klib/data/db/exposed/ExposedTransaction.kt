package klib.data.db.exposed

import klib.data.transaction.Transaction

public class ExposedTransaction(private val transaction: org.jetbrains.exposed.sql.Transaction) : Transaction {

    override suspend fun rollback(): Unit = transaction.rollback()

    override suspend fun commit(): Unit = transaction.commit()
}
