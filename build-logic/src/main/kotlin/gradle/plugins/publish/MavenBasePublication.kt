@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.publish

import com.vanniktech.maven.publish.MavenPublishBaseExtension
//import com.vanniktech.maven.publish.central.DropMavenCentralDeploymentTask.Companion.registerDropMavenCentralDeploymentTask
//import com.vanniktech.maven.publish.central.EnableAutomaticMavenCentralPublishingTask.Companion.registerEnableAutomaticMavenCentralPublishingTask
//import com.vanniktech.maven.publish.central.MavenCentralBuildService.Companion.registerMavenCentralBuildService
//import com.vanniktech.maven.publish.central.PrepareMavenCentralPublishingTask.Companion.registerPrepareMavenCentralPublishingTask
//import com.vanniktech.maven.publish.gradlePublishing
//import com.vanniktech.maven.publish.workaround.rootProjectBuildDir
//import org.gradle.api.credentials.PasswordCredentials
//import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

//context(project: Project)
//public fun MavenPublishBaseExtension.publishToMaven(automaticRelease: Boolean = false) {
//    mavenCentral.set(true)
//    mavenCentral.finalizeValue()
//
//    val localRepository = project.layout.buildDirectory.dir("publishing/mavenCentral")
//    val versionIsSnapshot = version.map { it.endsWith("-SNAPSHOT") }
//
//    val repository = project.gradlePublishing.repositories.maven { repo ->
//        repo.name = "mavenCentral"
//    }
//
//    project.afterEvaluate {
//        if (versionIsSnapshot.get()) {
//            repository.setUrl("https://central.sonatype.com/repository/maven-snapshots/")
//            repository.credentials(PasswordCredentials::class.java)
//        }
//        else {
//            repository.setUrl(localRepository.get().asFile)
//        }
//    }
//
//    val buildService = project.registerMavenCentralBuildService(
//        repositoryUsername = project.providers.gradleProperty("mavenCentralUsername"),
//        repositoryPassword = project.providers.gradleProperty("mavenCentralPassword"),
//        rootBuildDirectory = project.rootProjectBuildDir(),
//        buildEventsListenerRegistry = buildEventsListenerRegistry,
//    )
//
//    val prepareTask = project.tasks.registerPrepareMavenCentralPublishingTask(buildService, groupId, artifactId, version, localRepository)
//    val enableAutomaticTask = project.tasks.registerEnableAutomaticMavenCentralPublishingTask(buildService)
//
//    project.tasks.withType(PublishToMavenRepository::class.java).configureEach { publishTask ->
//        if (publishTask.name.endsWith("ToMavenCentralRepository")) {
//            publishTask.dependsOn(prepareTask)
//            if (automaticRelease) {
//                publishTask.dependsOn(enableAutomaticTask)
//            }
//        }
//    }
//
//    project.tasks.register("publishToMavenCentral") {
//        it.description = "Publishes to Maven Central"
//        it.group = "publishing"
//        it.dependsOn(project.tasks.named("publishAllPublicationsToMavenCentralRepository"))
//    }
//    project.tasks.register("publishAndReleaseToMavenCentral") {
//        it.description = "Publishes to Maven Central and automatically triggers release"
//        it.group = "publishing"
//        it.dependsOn(project.tasks.named("publishAllPublicationsToMavenCentralRepository"))
//        it.dependsOn(enableAutomaticTask)
//    }
//
//    project.tasks.registerDropMavenCentralDeploymentTask(buildService)
//}

public val Project.mavenPublishing: MavenPublishBaseExtension get() = the()

public fun Project.mavenPublishing(configure: MavenPublishBaseExtension.() -> Unit): Unit = extensions.configure(configure)
