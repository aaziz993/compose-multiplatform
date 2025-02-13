package gradle.amper.model.doctor

import kotlinx.serialization.Serializable

@Serializable
internal data class DoctorSettings(
    val enabled: Boolean = true,
    val enableTaskMonitoring: Boolean = true,
    val enableTestCaching: Boolean = true,
    val javaHome: JavaHome? = null,
)
