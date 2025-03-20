package gradle.plugins.android

import com.android.build.api.dsl.PostProcessing
import gradle.api.trySet
import java.util.SortedSet
import kotlinx.serialization.Serializable

/**
 * DSL object for configuring postProcessing: removing dead code, obfuscating etc.
 *
 * <p>This DSL is incubating and subject to change. To configure code and resource shrinkers,
 * Instead use the properties already available in the <a
 * href="com.android.build.gradle.internal.dsl.BuildType.html"><code>buildType</code></a> block.
 *
 * <p>To learn more, read <a
 * href="https://developer.android.com/studio/build/shrink-code.html">Shrink Your Code and
 * Resources</a>.
 */
@Serializable
internal data class PostProcessing(
    val isRemoveUnusedCode: Boolean? = null,
    val isRemoveUnusedResources: Boolean? = null,
    val isObfuscate: Boolean? = null,
    val isOptimizeCode: Boolean? = null,
    val proguardFiles: List<String>? = null,
    val setProguardFiles: List<String>? = null,
    val testProguardFiles: List<String>? = null,
    val setTestProguardFiles: List<String>? = null,
    val consumerProguardFiles: List<String>? = null,
    val setConsumerProguardFiles: List<String>? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(recipient: PostProcessing) {
        recipient::isRemoveUnusedCode trySet isRemoveUnusedCode
        recipient::isRemoveUnusedResources trySet isRemoveUnusedResources
        recipient::isObfuscate trySet isObfuscate
        recipient::isOptimizeCode trySet isOptimizeCode

        proguardFiles?.let { proguardFiles ->
            recipient.proguardFiles(*proguardFiles.toTypedArray())
        }

        setProguardFiles?.let(recipient::setProguardFiles)

        testProguardFiles?.let { testProguardFiles ->
            recipient.testProguardFiles(*testProguardFiles.toTypedArray())
        }

        testProguardFiles?.let(recipient::setTestProguardFiles)

        consumerProguardFiles?.let { consumerProguardFiles ->
            recipient.consumerProguardFiles(*consumerProguardFiles.toTypedArray())
        }

        consumerProguardFiles?.let(recipient::setConsumerProguardFiles)
    }
}
