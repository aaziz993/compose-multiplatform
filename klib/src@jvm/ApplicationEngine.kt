import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.network.tls.extensions.HashAlgorithm
import io.ktor.network.tls.extensions.SignatureAlgorithm
import java.io.File
import java.io.FileInputStream
import java.net.Inet4Address
import java.security.KeyStore
import javax.security.auth.x500.X500Principal

@Suppress("UnusedReceiverParameter")
public fun ApplicationEngine.Configuration.generateSelfSignedSSL(
    val keyStoreFile: String = File("build/keystore.p12"),
    val keyStorePassword: String,
    val keyAlias: String = "serverSSL",
    val hash: String? = null,
    val sign: String? = null,
    val privateKeyPassword: String,
    val subject: String? = null,
    val daysValid: Long? = null,
    val keySizeInBits: Int? = null,
    val domains: List<String>? = null,
    val ipAddresses: List<String>? = null,
    val format: String = "PKCS12",
    val rewrite: Boolean = false,
    val port: Int = 443,
): KeyStore = if ((keyStoreFile.exists() && !rewrite))
    KeyStore.getInstance(format).apply {
        load(FileInputStream(keyStoreFile), keyStorePassword.toCharArray())
    }
else buildKeyStore {
    certificate(keyAlias) {
        this@generateSelfSignedSSL.hash?.let { hash = HashAlgorithm.valueOf(it) }
        this@generateSelfSignedSSL.sign?.let { sign = SignatureAlgorithm.valueOf(it) }
        this@generateSelfSignedSSL = privateKeyPassword
        this@generateSelfSignedSSL.subject?.let { subject = X500Principal(it) }
        this@generateSelfSignedSSL.daysValid?.let { daysValid = it }
        this@generateSelfSignedSSL.keySizeInBits?.let { keySizeInBits = it }
        this@generateSelfSignedSSL.domains?.let { domains = it }
        this@generateSelfSignedSSL.ipAddresses?.let { ipAddresses = it.map(Inet4Address::getByName) }
    }
}.also { keyStore -> keyStore.saveToFile(keyStoreFile, keyStorePassword) }
