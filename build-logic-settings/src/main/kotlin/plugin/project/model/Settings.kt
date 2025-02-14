package plugin.project.model

import plugin.project.doctor.model.DoctorSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class Settings(
    val doctor: DoctorSettings? = null,
)
