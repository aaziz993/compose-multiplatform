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

public val Project.mavenPublishing: MavenPublishBaseExtension get() = the()

public fun Project.mavenPublishing(configure: MavenPublishBaseExtension.() -> Unit): Unit = extensions.configure(configure)
