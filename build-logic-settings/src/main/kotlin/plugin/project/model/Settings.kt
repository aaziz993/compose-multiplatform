package plugin.project.model

import plugin.project.doctor.DoctorSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class Settings(
    val doctor: DoctorSettings? = null,
)
