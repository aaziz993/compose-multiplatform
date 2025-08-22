package gradle.api.ci

public data class Versioning(
    public val branch: Boolean = true,
    public val run: Boolean = true
)
