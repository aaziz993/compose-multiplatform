package klib.data.transaction

public interface Transaction {

    public fun rollback()

    public fun commit()
}
