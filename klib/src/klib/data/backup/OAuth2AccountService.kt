package klib.data.backup

public sealed interface OAuth2AccountService {

    public interface Initialized : OAuth2AccountService {

        public fun requestPermissions(): RequestedPermissions?
    }

    public interface RequestedPermissions : OAuth2AccountService {

        public fun generateVerifyUri(): String

        public suspend fun authenticateUser(userCode: String): Authenticated
    }

    public interface Authenticated : OAuth2AccountService {

        public val isExpired: Boolean

        public fun updateUserLinkedAccounts(linkedAccounts: UserLinkedAccountsModel): UserLinkedAccountsModel

        public suspend fun refreshUserAccessToken(): Authenticated

        public suspend fun uploadBackupData(data: ByteArray): Boolean

        public suspend fun downloadBackupData(): ByteArray?
    }
}
