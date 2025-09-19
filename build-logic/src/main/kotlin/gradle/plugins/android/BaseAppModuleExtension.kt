package gradle.plugins.android

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

import gradle.api.project.sensitive
import gradle.api.project.sensitiveOrElse
import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Security
import java.util.Date
import java.util.concurrent.TimeUnit
import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.gradle.api.Project

context(project: Project)
public fun BaseAppModuleExtension.signingConfig(
    name: String,
    storeType: String = project.sensitiveOrElse("android.$name.signing.store.type") { "PKCS12" },
    storeFile: File = project.file(".signing.android.$name.$storeType"),
    storePassword: String = project.sensitive("android.$name.signing.store.password"),
    keyAlgorithm: String = project.sensitiveOrElse("android.$name.signing.key.algorithm") { "RSA" },
    keySize: Int = project.sensitiveOrElse("android.$name.signing.key.size") { "4096" }.toInt(),
    keyAlias: String = project.sensitiveOrElse("android.$name.signing.key.alias") { "androidSigning" },
    keyPassword: String = project.sensitive("android.$name.signing.key.password"),
    dn: String = "CN=Unknown, OU=Dev, O=Company, L=City, ST=State, C=US",
    expire: Long = 3650,
    isV2SigningEnabled: Boolean = project.sensitiveOrElse("android.$name.signing.v2") { "true" }.toBoolean(),
) {
    generateAndroidKeystore(
        storeType,
        storeFile,
        storePassword,
        keyAlgorithm,
        keySize,
        keyAlias,
        keyPassword,
        dn,
        expire,
    )

    signingConfigs.create(name) {
        this.storeType = storeType
        this.storeFile = storeFile
        this.storePassword = storePassword
        this.keyAlias = keyAlias
        this.keyPassword = keyPassword
        this.isV2SigningEnabled = isV2SigningEnabled
    }
}

private fun generateAndroidKeystore(
    storeType: String = "PKCS12",
    storeFile: File,
    storePassword: String,
    keyAlgorithm: String,
    keySize: Int = 4096,
    keyAlias: String,
    keyPassword: String,
    dn: String = "CN=Unknown, OU=Dev, O=Company, L=City, ST=State, C=US",
    expire: Long = 3650
) {
    if (storeFile.exists()) return

    // Ensure BouncyCastle provider is registered
    Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) ?: Security.addProvider(BouncyCastleProvider())

    // Generate key pair
    val keyPairGen = KeyPairGenerator.getInstance(keyAlgorithm, "BC")
    keyPairGen.initialize(keySize)
    val keyPair = keyPairGen.generateKeyPair()

    val now = Date()

    // Handle expire = 0L as unexpired → use far future date
    val endDate = if (expire > 0L) Date(now.time + TimeUnit.SECONDS.toMillis(expire))
    else Date(Long.MAX_VALUE)

    // Build X.509 certificate
    val certBuilder = JcaX509v3CertificateBuilder(
        X500Name(dn),
        BigInteger.valueOf(System.currentTimeMillis()),
        now,
        endDate,
        X500Name(dn),
        keyPair.public,
    )

    val sigAlg = when (keyAlgorithm.uppercase()) {
        "RSA" -> "SHA256withRSA"
        "EC" -> "SHA256withECDSA"
        else -> "SHA256withRSA"
    }

    val signer = JcaContentSignerBuilder(sigAlg)
        .setProvider("BC")
        .build(keyPair.private)

    val certificate = JcaX509CertificateConverter()
        .setProvider("BC")
        .getCertificate(certBuilder.build(signer))

    // Create PKCS12 keystore
    val keyStore = KeyStore.getInstance(storeType)
    keyStore.load(null, storePassword.toCharArray())
    keyStore.setKeyEntry(keyAlias, keyPair.private, keyPassword.toCharArray(), arrayOf(certificate))

    FileOutputStream(storeFile).use { fos ->
        keyStore.store(fos, storePassword.toCharArray())
    }
}
