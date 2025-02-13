package plugin.project.model

import plugin.project.amper.doctor.DoctorSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class Settings(
    val doctor: DoctorSettings? = null,
)
