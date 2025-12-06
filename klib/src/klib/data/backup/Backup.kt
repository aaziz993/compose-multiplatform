package klib.data.backup

public interface Backup {

//    public fun updateUserLinkedAccounts(linkedAccounts: UserLinkedAccountsModel): UserLinkedAccountsModel

    public suspend fun upload(path: String, data: ByteArray): Boolean

    public suspend fun download(path: String): ByteArray?
}
