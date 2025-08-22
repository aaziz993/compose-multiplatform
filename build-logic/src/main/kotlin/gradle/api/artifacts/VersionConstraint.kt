package gradle.api.artifacts

import klib.data.type.serialization.serializers.transform.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

@KeepGeneratedSerializer
@Serializable(with = VersionConstraintTransformingSerializer::class)
public data class VersionConstraint(
    private val branch: String? = null,
    private val require: String,
    private val prefer: String = require,
    private val strictly: String = require,
    private val reject: List<String> = emptyList(),
) : org.gradle.api.artifacts.VersionConstraint {

    override fun getBranch(): String? = branch

    override fun getRequiredVersion(): String = require

    override fun getPreferredVersion(): String = prefer

    override fun getStrictVersion(): String = strictly

    override fun getRejectedVersions(): List<String> = reject

    override fun getDisplayName(): String = require

    override fun toString(): String = require
}

private object VersionConstraintTransformingSerializer :
    MapTransformingSerializer<VersionConstraint>(
        VersionConstraint.generatedSerializer(),
        "require",
    )
