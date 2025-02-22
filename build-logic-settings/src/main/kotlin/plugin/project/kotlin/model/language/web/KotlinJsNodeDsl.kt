package plugin.project.kotlin.model.language.web

import kotlinx.serialization.Serializable

@Serializable

internal data class KotlinJsNodeDsl(
    override val distribution: Distribution?,
    override val testTask: KotlinJsTest?,
    val runTask: NodeJsExec? = null,
    val passProcessArgvToMainFunction: Boolean? = null,
) : KotlinJsSubTargetDsl
