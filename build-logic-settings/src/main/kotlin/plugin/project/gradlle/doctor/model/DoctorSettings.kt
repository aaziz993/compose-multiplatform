package plugin.project.gradlle.doctor.model

import kotlinx.serialization.Serializable

@Serializable
internal data class DoctorSettings(
    val enabled: Boolean = true,
    val enableTaskMonitoring: Boolean = true,
    val enableTestCaching: Boolean? = null,
    val javaHome: JavaHome? = null,
)
