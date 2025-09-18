package config

import io.ktor.network.tls.certificates.*
import io.ktor.network.tls.extensions.*
import io.ktor.server.engine.*
import java.io.File
import java.io.FileInputStream
import java.net.Inet4Address
import java.security.KeyStore
import javax.security.auth.x500.X500Principal

public fun ApplicationEngine.Configuration.ssl(
    keyStoreFile: File = File("resources/security/config.ssl/keystore.p12"),
    keyStorePassword: String,
    keyAlias: String = "server",
    hash: String? = null,
    sign: String? = null,
    privateKeyPassword: String,
    subject: String? = null,
    daysValid: Long? = null,
    keySizeInBits: Int? = null,
    domains: List<String>? = null,
    ipAddresses: List<String>? = null,
    format: String = "PKCS12",
    port: Int = 443,
) {
    val keyStore = if (keyStoreFile.exists())
        KeyStore.getInstance(format).apply {
            load(FileInputStream(keyStoreFile), keyStorePassword.toCharArray())
        }
    else buildKeyStore {
        certificate(keyAlias) {
            hash?.let { hash -> this.hash = HashAlgorithm.valueOf(hash) }
            sign?.let { sign -> this.sign = SignatureAlgorithm.valueOf(sign) }
            password = privateKeyPassword
            subject?.let { subject -> this.subject = X500Principal(subject) }
            daysValid?.let { daysValid -> this.daysValid = daysValid }
            keySizeInBits?.let { keySizeInBits -> this.keySizeInBits = keySizeInBits }
            domains?.let { domains -> this.domains = domains }
            ipAddresses?.let { ipAddresses -> this.ipAddresses = ipAddresses.map(Inet4Address::getByName) }
        }
    }

    keyStore.saveToFile(keyStoreFile, keyStorePassword)

    sslConnector(
        keyStore = keyStore,
        keyAlias = keyAlias,
        keyStorePassword = { keyStorePassword.toCharArray() },
        privateKeyPassword = { privateKeyPassword.toCharArray() },
    ) {
        this.port = port
        this.keyStorePath = keyStoreFile
    }
}
