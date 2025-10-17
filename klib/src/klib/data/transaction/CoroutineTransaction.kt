package klib.data.transaction

public interface CoroutineTransaction {

    public suspend fun rollback()

    public suspend fun commit()
}
