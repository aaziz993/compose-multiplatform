package plugin.project.model

import kotlinx.serialization.Serializable
import plugin.project.gradle.dokka.model.DokkaMultiModuleTask
import plugin.project.gradle.dokka.model.DokkaTask

@Serializable
internal data class Tasks(
    val dokkaTask: DokkaTask? = null,
    val dokkaMultiModuleTask: DokkaMultiModuleTask? = null,
)
