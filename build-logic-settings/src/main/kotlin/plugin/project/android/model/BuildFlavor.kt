package plugin.project.android.model

@Suppress("EnumEntryName")
public enum class BuildFlavor(public val dimension: FlavorDimension, public val applicationIdSuffix: String? = null) {

    demo(FlavorDimension.contentType)
}
