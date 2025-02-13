package gradle.amper.model

import gradle.amper.model.doctor.DoctorSettings
import kotlinx.serialization.Serializable

@Serializable
internal data class Settings(
    val doctor: DoctorSettings? = null,
)
