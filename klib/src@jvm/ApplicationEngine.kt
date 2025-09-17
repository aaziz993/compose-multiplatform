import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.network.tls.extensions.HashAlgorithm
import io.ktor.network.tls.extensions.SignatureAlgorithm
import io.ktor.server.engine.ApplicationEngine
import java.io.File
import java.io.FileInputStream
import java.net.Inet4Address
import java.security.KeyStore
import javax.security.auth.x500.X500Principal

public fun ApplicationEngine.Configuration.generateSelfSignedSSL(
    keyStoreFile: File = File("build/keystore.p12"),
    keyStorePassword: String,
    keyAlias: String = "serverSSL",
    hash: String? = null,
    sign: String? = null,
    privateKeyPassword: String,
    subject: String? = null,
    daysValid: Long? = null,
    keySizeInBits: Int? = null,
    domains: List<String>? = null,
    ipAddresses: List<String>? = null,
    format: String = "PKCS12",
    rewrite: Boolean = false,
): KeyStore = if ((keyStoreFile.exists() && !rewrite))
    KeyStore.getInstance(format).apply {
        load(FileInputStream(keyStoreFile), keyStorePassword.toCharArray())
    }
else buildKeyStore {
    certificate(keyAlias) {
        hash?.let { this.hash = HashAlgorithm.valueOf(it) }
        sign?.let { this.sign = SignatureAlgorithm.valueOf(it) }
        this.password = privateKeyPassword
        subject?.let { this.subject = X500Principal(it) }
        daysValid?.let { this.daysValid = it }
        keySizeInBits?.let { this.keySizeInBits = it }
        domains?.let { this.domains = it }
        ipAddresses?.let { this.ipAddresses = it.map(Inet4Address::getByName) }
    }
}.also { keyStore -> keyStore.saveToFile(keyStoreFile, keyStorePassword) }
