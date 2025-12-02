package config

import io.ktor.network.tls.certificates.*
import io.ktor.network.tls.extensions.*
import io.ktor.server.engine.*
import java.io.File
import java.io.FileInputStream
import java.net.InetAddress
import java.security.KeyStore
import java.util.concurrent.TimeUnit
import javax.security.auth.x500.X500Principal

public fun ApplicationEngine.Configuration.ssl(
    keyStoreType: String = "PKCS12",
    keyStoreFile: File = File("resources/security/ssl/.keystore.$keyStoreType"),
    keyStorePassword: String,
    keySize: Int? = null,
    keyAlias: String = "server",
    keyPassword: String,
    subject: String? = null,
    domains: List<String>? = null,
    ipAddresses: List<String>? = null,
    hash: String? = null,
    sign: String? = null,
    expire: Long = 0L,
    port: Int = 443,
) {
    val keyStore = if (keyStoreFile.exists())
        KeyStore.getInstance(keyStoreType).apply {
            load(FileInputStream(keyStoreFile), keyStorePassword.toCharArray())
        }
    else buildKeyStore {
        certificate(keyAlias) {
            password = keyPassword
            subject?.let { subject -> this.subject = X500Principal(subject) }
            keySize?.let { keySize -> this.keySizeInBits = keySize * 8 }
            domains?.let { domains -> this.domains = domains }
            ipAddresses?.let { ipAddresses -> this.ipAddresses = ipAddresses.map(InetAddress::getByName) }
            hash?.let { hash -> this.hash = HashAlgorithm.valueOf(hash) }
            sign?.let { sign -> this.sign = SignatureAlgorithm.valueOf(sign) }
            expire.takeIf { expire -> expire > 0L }?.let { expire -> this.daysValid = TimeUnit.SECONDS.toDays(expire) }
        }
    }.also { keyStore -> keyStore.saveToFile(keyStoreFile, keyStorePassword) }

    sslConnector(
        keyStore = keyStore,
        keyStorePassword = { keyStorePassword.toCharArray() },
        keyAlias = keyAlias,
        privateKeyPassword = { keyPassword.toCharArray() },
    ) {
        this.keyStorePath = keyStoreFile
        this.port = port
    }
}
