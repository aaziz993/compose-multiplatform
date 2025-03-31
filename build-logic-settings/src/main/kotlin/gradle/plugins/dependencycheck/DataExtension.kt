package gradle.plugins.dependencycheck

import gradle.reflect.trySet
import kotlinx.serialization.Serializable
import org.owasp.dependencycheck.gradle.extension.DataExtension

@Serializable
internal data class DataExtension(
    /**
     * The directory to store the H2 database that contains the cache of the NVD CVE data.
     */
    val directory: String? = null,
    /**
     * The connection string to the database.
     */
    val connectionString: String? = null,
    /**
     * The user name to use when connecting to the database.
     */
    val username: String? = null,
    /**
     * The password to use when connecting to the database.
     */
    val password: String? = null,
    /**
     * The database driver name (e.g. org.h2.Driver).
     */
    val driver: String? = null,
    /**
     * The path to the driver (JAR) in case it is not already in the classpath.
     */
    val driverPath: String? = null,
) {

    fun applyTo(receiver: DataExtension) {
        receiver::setDirectory trySet directory
        receiver::setConnectionString trySet connectionString
        receiver::setUsername trySet username
        receiver::setPassword trySet password
        receiver::setDriver trySet driver
        receiver::setDriverPath trySet driverPath
    }
}
