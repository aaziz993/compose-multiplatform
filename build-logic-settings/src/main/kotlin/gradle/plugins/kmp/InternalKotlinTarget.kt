@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.kmp

import org.gradle.kotlin.dsl.withType
import gradle.accessors.publishing
import gradle.api.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetComponent
import org.jetbrains.kotlin.gradle.plugin.mpp.AbstractKotlinTarget

internal interface InternalKotlinTarget<T : org.jetbrains.kotlin.gradle.plugin.mpp.InternalKotlinTarget> : KotlinTarget<T> {

    val onPublicationCreated: String?

    context(Project)
    @OptIn(InternalKotlinGradlePluginApi::class)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        onPublicationCreated
            ?.let(project.publishing.publications.withType<org.gradle.api.publish.maven.MavenPublication>()::getByName)
            ?.let { publication ->
                receiver.onPublicationCreated(publication)
            }
    }
}
