package klib.data.transaction

public interface Transaction {

    public suspend fun rollback()

    public suspend fun commit()
}
