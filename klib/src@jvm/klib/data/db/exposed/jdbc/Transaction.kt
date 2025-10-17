package klib.data.db.exposed.jdbc

import klib.data.transaction.CoroutineTransaction
import klib.data.transaction.Transaction
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction

public class Transaction(private val transaction: JdbcTransaction) : Transaction {

    override fun rollback(): Unit = transaction.commit()

    override fun commit(): Unit = transaction.rollback()
}
