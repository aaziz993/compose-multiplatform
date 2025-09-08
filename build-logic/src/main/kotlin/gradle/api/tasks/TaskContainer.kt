package gradle.api.tasks

import gradle.api.maybeNamed
import kotlinx.validation.KotlinApiBuildTask
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.tasks.TaskContainer
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

@Suppress("UnusedReceiverParameter")
context(project: Project)
public val TaskContainer.apiBuild: NamedDomainObjectProvider<KotlinApiBuildTask>?
    get() = project.tasks.maybeNamed("apiBuild", KotlinApiBuildTask::class.java)

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun TaskContainer.apiBuild(configure: KotlinApiBuildTask.() -> Unit): NamedDomainObjectProvider<KotlinApiBuildTask>? =
    project.tasks.maybeNamed("apiBuild", KotlinApiBuildTask::class.java, configure)

@Suppress("UnusedReceiverParameter")
context(project: Project)
public val TaskContainer.dokkaGeneratePublicationHtml: NamedDomainObjectProvider<DokkaGeneratePublicationTask>?
    get() = project.tasks.maybeNamed("dokkaGeneratePublicationHtml", DokkaGeneratePublicationTask::class.java)

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun TaskContainer.dokkaGeneratePublicationHtml(configure: KotlinApiBuildTask.() -> Unit): NamedDomainObjectProvider<KotlinApiBuildTask>? =
    project.tasks.maybeNamed("apiBuild", KotlinApiBuildTask::class.java, configure)

@Suppress("UnusedReceiverParameter")
context(project: Project)
public val TaskContainer.dokkaGeneratePublicationJavadoc: NamedDomainObjectProvider<DokkaGeneratePublicationTask>?
    get() = project.tasks.maybeNamed("dokkaGeneratePublicationJavadoc", DokkaGeneratePublicationTask::class.java)

@Suppress("UnusedReceiverParameter")
context(project: Project)
public fun TaskContainer.dokkaGeneratePublicationJavadoc(configure: KotlinApiBuildTask.() -> Unit): NamedDomainObjectProvider<KotlinApiBuildTask>? =
    project.tasks.maybeNamed("apiBuild", KotlinApiBuildTask::class.java, configure)
