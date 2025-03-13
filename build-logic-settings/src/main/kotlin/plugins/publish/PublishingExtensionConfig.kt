package plugin.project.gradle.publish

import gradle.androidNativeTargets
import gradle.findByName
import gradle.darwinTargets
import gradle.hasAndroidNative
import gradle.hasDarwin
import gradle.hasJs
import gradle.hasLinux
import gradle.hasWasmJs
import gradle.hasWindows
import gradle.jsTargets
import gradle.jvmAndCommonTargets
import gradle.linuxTargets
import gradle.api.maybeNamed
import gradle.windowsTargets
import io.github.z4kn4fein.semver.Version
import java.util.concurrent.locks.ReentrantLock
import org.gradle.api.Project
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.internal.os.OperatingSystem
import org.gradle.jvm.tasks.Jar
import org.gradle.accessors.kotlin.dsl.apply
import org.gradle.accessors.kotlin.dsl.assign
import org.gradle.accessors.kotlin.dsl.extra
import org.gradle.accessors.kotlin.dsl.getByName
import org.gradle.accessors.kotlin.dsl.invoke
import org.gradle.accessors.kotlin.dsl.provideDelegate
import org.gradle.accessors.kotlin.dsl.register
import org.gradle.accessors.kotlin.dsl.settings
import org.gradle.accessors.kotlin.dsl.withType
import org.gradle.accessors.plugins.signing.Sign
import org.gradle.accessors.plugins.signing.SigningExtension
import org.gradle.accessors.plugins.signing.SigningPlugin

internal fun Project.configurePublishingExtension(extension: PublishingExtension) = extension.apply {
    with(settings.extension) {
        afterEvaluate {
            apply(plugin = "maven-publish")

            tasks.withType<AbstractPublishToMaven>().configureEach {
                onlyIf { publication.isAvailableForPublication() }
            }

            configureAggregatingTasks()


            publications.withType(MavenPublication::class.java).configureEach {
                pom {
                    name = project.name
                    description = project.description.orEmpty()
                        .ifEmpty { "Kotlin libraries for quickly creating applications in Kotlin with minimal effort." }
                    inceptionYear = projectInceptionYear

                    url = "https://github.com/$githubUsername/${rootProject.name}/${project.name}"

                    licenses {
                        license {
                            name = projectLicenseName
                            url = projectLicenseTextUrl
                            distribution = projectLicenseDistribution
                        }
                    }

                    issueManagement {
                        system = "GitHub Issues"
                        url = "https://github.com/$githubUsername/${rootProject.name}/issues" // Change here
                    }

                    developers {
                        developer {
                            id = projectDeveloperId
                            name = projectDeveloperName
                            email = projectDeveloperEmail
                            providers.gradleProperty("project.developer.organization.name").orNull?.let {
                                organization = it
                            }
                            providers.gradleProperty("project.developer.organization.url").orNull?.let {
                                organizationUrl = it
                            }
                        }
                    }

                    scm {
                        connection = "scm:git:git://github.com:$githubUsername/${rootProject.name}.git"
                        url = "https://github.com/$githubUsername/${rootProject.name}"
                        developerConnection = "scm:git:ssh://github.com:$githubUsername/${rootProject.name}.git"
                    }
                }
            }
        }
    }

    tasks.named("publish") {
        dependsOn(tasks.named("publishToMavenLocal"))
    }

    configureSigning()
    configureJavadocArtifact()
}

private fun Publication.isAvailableForPublication(): Boolean {
    val name = name
    val os = OperatingSystem.current()

    var result = name in jvmAndCommonTargets || name in jsTargets || name in androidNativeTargets
    result = result || (os == OperatingSystem.LINUX && name in linuxTargets)
    result = result || (os == OperatingSystem.WINDOWS && name in windowsTargets)
    result = result || (os == OperatingSystem.MAC_OS && name in darwinTargets)

    return result
}

private fun Project.configureAggregatingTasks() {
    registerAggregatingTask("JvmAndCommon", jvmAndCommonTargets)
    if (hasJs || hasWasmJs) registerAggregatingTask("Js", jsTargets)
    if (hasLinux) registerAggregatingTask("Linux", linuxTargets)
    if (hasWindows) registerAggregatingTask("Windows", windowsTargets)
    if (hasDarwin) registerAggregatingTask("Darwin", darwinTargets)
    if (hasAndroidNative) registerAggregatingTask("AndroidNative", androidNativeTargets)
}

private fun Project.registerAggregatingTask(name: String, targets: Set<String>) {
    tasks.register("publish${name}Publications") {
        group = PublishingPlugin.PUBLISH_TASK_GROUP
        val targetsTasks = targets.mapNotNull { target ->
            tasks.maybeNamed("publish${target.capitalized()}PublicationToMavenRepository")
        }
        dependsOn(targetsTasks)
    }
}




// Extension accessors
