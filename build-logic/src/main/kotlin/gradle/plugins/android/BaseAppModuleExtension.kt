import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import gradle.api.project.sensitive
import gradle.api.project.sensitiveOrElse

import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Security
import java.security.cert.X509Certificate
import java.util.Date
import kotlin.String
import org.gradle.api.Project

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun BaseAppModuleExtension.signingConfig(
    storeFile: File = project.file("resources@android/release.keystore"),
    storePassword: String = project.sensitive("android.release.signing.store.password"),
    keyPassword: String = project.sensitive("android.release.signing.key.password"),
    keyAlias: String = project.sensitiveOrElse("android.release.signing.key.alias") { "signing" },
    storeType: String = project.sensitiveOrElse("android.release.signing.store.type") { "PKCS12" },
    isV2SigningEnabled: Boolean = true
) {
    val fileName = "$name.${storeType.lowercase()}"
}
