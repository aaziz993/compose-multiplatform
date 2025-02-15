package plugin.project.gradlle.model

import kotlinx.serialization.Serializable
import plugin.project.gradlle.doctor.model.DoctorSettings

@Serializable
internal data class GradleSettings(
    val doctor: DoctorSettings = DoctorSettings(),
)
