package gradle.api.artifacts

import klib.data.type.collections.takeIfNotEmpty
import klib.data.type.functions.tryInvoke
import klib.data.type.serialization.serializers.transform.MapTransformingSerializer
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.gradle.api.internal.artifacts.dependencies.DefaultMutableVersionConstraint

@KeepGeneratedSerializer
@Serializable(with = VersionConstraintTransformingSerializer::class)
public data class VersionConstraint(
    private val branch: String? = null,
    private val require: String,
    private val prefer: String = require,
    private val strictly: String = require,
    private val reject: List<String> = emptyList(),
    @Transient
    private val vc: org.gradle.api.artifacts.VersionConstraint = DefaultMutableVersionConstraint(require).apply {
        ::setBranch tryInvoke branch
        ::prefer tryInvoke prefer.takeIf(String::isNotBlank)
        ::strictly tryInvoke strictly.takeIf(String::isNotBlank)
        ::reject tryInvoke reject.takeIfNotEmpty()?.toTypedArray()
    }.asImmutable()
) : org.gradle.api.artifacts.VersionConstraint by vc

private object VersionConstraintTransformingSerializer :
    MapTransformingSerializer<VersionConstraint>(
        VersionConstraint.generatedSerializer(),
        "require",
    )
