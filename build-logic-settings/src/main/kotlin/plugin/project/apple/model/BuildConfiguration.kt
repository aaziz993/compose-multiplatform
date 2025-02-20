package plugin.project.apple.model

internal data class BuildConfiguration (
    val buildType: String?=null,
    val fatFrameworks: kotlin.Boolean?=null,
    val name: String?=null,
    val properties: Map<String,Any>
)
