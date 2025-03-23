package gradle.plugins.kotlin.apollo.tasks

import org.gradle.kotlin.dsl.withType
import com.apollographql.apollo3.gradle.internal.ApolloGenerateUsedCoordinatesAndCheckFragmentsTask
import kotlinx.serialization.Serializable
import gradle.api.tasks.DefaultTask
import org.gradle.api.Project
import gradle.collection.SerializableAnyMap
import gradle.api.tasks.applyTo

@Serializable
internal data class ApolloGenerateUsedCoordinatesAndCheckFragmentsTask(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
): DefaultTask<ApolloGenerateUsedCoordinatesAndCheckFragmentsTask>(){
context(Project)
override fun applyTo(recipient: ApolloGenerateUsedCoordinatesAndCheckFragmentsTask){
super.applyTo(recipient)

}

context(Project)
override fun applyTo() =
applyTo(tasks.withType<ApolloGenerateUsedCoordinatesAndCheckFragmentsTask>())
}

    val downStreamIrOperations: Set<String>?=null,
val setDownStreamIrOperations: Set<String>?=null,


    val irOperations: String?=null,


    val outputFile: String?=null,



}
