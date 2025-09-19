import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

import java.io.File
import java.io.FileOutputStream
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Security
import java.security.cert.X509Certificate
import java.util.Date
import org.gradle.api.Project

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun BaseAppModuleExtension.signingConfig(
    name: String,
    storePassword: String,
    keyPassword: String,
    keyAlias: String = "signing",
    storeType: String = "PKCS12",
    isV2SigningEnabled: Boolean = true,
) {
    val fileName = "$name.${storeType.lowercase()}"
}
