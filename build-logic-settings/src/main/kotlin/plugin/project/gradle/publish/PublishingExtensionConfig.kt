package plugin.project.gradle.publish//package plugin.project.gradle.publish
//
//import gradle.androidNativeTargets
//import gradle.findByName
//import gradle.darwinTargets
//import gradle.hasAndroidNative
//import gradle.hasDarwin
//import gradle.hasJs
//import gradle.hasLinux
//import gradle.hasWasmJs
//import gradle.hasWindows
//import gradle.jsTargets
//import gradle.jvmAndCommonTargets
//import gradle.linuxTargets
//import gradle.maybeNamed
//import gradle.windowsTargets
//import io.github.z4kn4fein.semver.Version
//import java.util.concurrent.locks.ReentrantLock
//import org.gradle.api.Project
//import org.gradle.api.publish.Publication
//import org.gradle.api.publish.PublishingExtension
//import org.gradle.api.publish.maven.MavenPublication
//import org.gradle.api.publish.maven.tasks.AbstractPublishToMaven
//import org.gradle.api.publish.plugins.PublishingPlugin
//import org.gradle.internal.extensions.stdlib.capitalized
//import org.gradle.internal.os.OperatingSystem
//import org.gradle.jvm.tasks.Jar
//import org.gradle.kotlin.dsl.apply
//import org.gradle.kotlin.dsl.assign
//import org.gradle.kotlin.dsl.extra
//import org.gradle.kotlin.dsl.getByName
//import org.gradle.kotlin.dsl.invoke
//import org.gradle.kotlin.dsl.provideDelegate
//import org.gradle.kotlin.dsl.register
//import org.gradle.kotlin.dsl.settings
//import org.gradle.kotlin.dsl.withType
//import org.gradle.plugins.signing.Sign
//import org.gradle.plugins.signing.SigningExtension
//import org.gradle.plugins.signing.SigningPlugin
//
//internal fun Project.configurePublishingExtension(extension: PublishingExtension) = extension.apply {
//    with(settings.extension) {
//        afterEvaluate {
//            apply(plugin = "maven-publish")
//
//            tasks.withType<AbstractPublishToMaven>().configureEach {
//                onlyIf { publication.isAvailableForPublication() }
//            }
//
//            configureAggregatingTasks()
//
//
//            repositories {
//                // SPACE PACKAGES
//                maven {
//                    name = "SpacePackages"
//
//                    url = uri(providers.gradleProperty("jetbrains.space.packages.url").get())
//
//                    credentials {
//                        username = System.getenv().getOrElse("JB_SPACE_USERNAME") {
//                            providers.gradleProperty("jetbrains.space.username").get()
//                        }
//                        password = System.getenv().getOrElse("JB_SPACE_PASSWORD") {
//                            localProperties.getProperty("jetbrains.space.password")
//                        }
//                    }
//                }
//
//                // GITHUB PACKAGES
//                maven {
//                    name = "GithubPackages"
//
//                    url = uri(
//                        "${
//                            providers.gradleProperty("github.packages.url").get()
//                        }/${rootProject.name}",
//                    )
//
//                    // Repository username and password
//                    credentials {
//                        username = githubUsername
//
//                        password = System.getenv().getOrElse("GITHUB_PASSWORD") {
//                            localProperties.getProperty("github.password")
//                        }
//                    }
//                }
//
//                // TEST LOCAL
//                maven {
//                    name = "testLocal"
//                    setUrl(rootProject.layout.buildDirectory.dir("m2"))
//                }
//            }
//
//
//            publications.withType(MavenPublication::class.java).configureEach {
//                pom {
//                    name = project.name
//                    description = project.description.orEmpty()
//                        .ifEmpty { "Kotlin libraries for quickly creating applications in Kotlin with minimal effort." }
//                    inceptionYear = projectInceptionYear
//
//                    url = "https://github.com/$githubUsername/${rootProject.name}/${project.name}"
//
//                    licenses {
//                        license {
//                            name = projectLicenseName
//                            url = projectLicenseTextUrl
//                            distribution = projectLicenseDistribution
//                        }
//                    }
//
//                    issueManagement {
//                        system = "GitHub Issues"
//                        url = "https://github.com/$githubUsername/${rootProject.name}/issues" // Change here
//                    }
//
//                    developers {
//                        developer {
//                            id = projectDeveloperId
//                            name = projectDeveloperName
//                            email = projectDeveloperEmail
//                            providers.gradleProperty("project.developer.organization.name").orNull?.let {
//                                organization = it
//                            }
//                            providers.gradleProperty("project.developer.organization.url").orNull?.let {
//                                organizationUrl = it
//                            }
//                        }
//                    }
//
//                    scm {
//                        connection = "scm:git:git://github.com:$githubUsername/${rootProject.name}.git"
//                        url = "https://github.com/$githubUsername/${rootProject.name}"
//                        developerConnection = "scm:git:ssh://github.com:$githubUsername/${rootProject.name}.git"
//                    }
//                }
//            }
//        }
//    }
//
//    tasks.named("publish") {
//        dependsOn(tasks.named("publishToMavenLocal"))
//    }
//
//    configureSigning()
//    configureJavadocArtifact()
//}
//
//private fun Publication.isAvailableForPublication(): Boolean {
//    val name = name
//    val os = OperatingSystem.current()
//
//    var result = name in jvmAndCommonTargets || name in jsTargets || name in androidNativeTargets
//    result = result || (os == OperatingSystem.LINUX && name in linuxTargets)
//    result = result || (os == OperatingSystem.WINDOWS && name in windowsTargets)
//    result = result || (os == OperatingSystem.MAC_OS && name in darwinTargets)
//
//    return result
//}
//
//private fun Project.configureAggregatingTasks() {
//    registerAggregatingTask("JvmAndCommon", jvmAndCommonTargets)
//    if (hasJs || hasWasmJs) registerAggregatingTask("Js", jsTargets)
//    if (hasLinux) registerAggregatingTask("Linux", linuxTargets)
//    if (hasWindows) registerAggregatingTask("Windows", windowsTargets)
//    if (hasDarwin) registerAggregatingTask("Darwin", darwinTargets)
//    if (hasAndroidNative) registerAggregatingTask("AndroidNative", androidNativeTargets)
//}
//
//private fun Project.registerAggregatingTask(name: String, targets: Set<String>) {
//    tasks.register("publish${name}Publications") {
//        group = PublishingPlugin.PUBLISH_TASK_GROUP
//        val targetsTasks = targets.mapNotNull { target ->
//            tasks.maybeNamed("publish${target.capitalized()}PublicationToMavenRepository")
//        }
//        dependsOn(targetsTasks)
//    }
//}
//
//private fun Project.configureSigning() {
//    extra["signing.gnupg.keyName"] = (System.getenv("SIGN_KEY_ID") ?: return)
//    extra["signing.gnupg.passphrase"] = (System.getenv("SIGN_KEY_PASSPHRASE") ?: return)
//
//    apply(SigningPlugin::class.java)
//
//
//
//    signing {
//        isRequired = !Version.parse(version.toString()).isPreRelease
//
//        useGpgCmd()
//
//        publishing.publications.withType<MavenPublication>().configureEach(::sign)
//    }
//
//    val gpgAgentLock: ReentrantLock by rootProject.extra { ReentrantLock() }
//
//    tasks.withType<Sign>().configureEach {
//        doFirst { gpgAgentLock.lock() }
//        doLast { gpgAgentLock.unlock() }
//    }
//}
//
//private fun Project.configureJavadocArtifact() {
//    val nonDefaultProjectStructure: List<String> by rootProject.extra
//    if (project.name in nonDefaultProjectStructure) return
//
//    val emptyJar = tasks.register<Jar>("emptyJar") {
//        archiveAppendix = "empty"
//    }
//
//    publishing {
//        for (target in kotlin.targets) {
//            val publication = publications.findByName<MavenPublication>(target.name) ?: continue
//
//            if (target.platformType.name == "jvm") {
//                publication.artifact(emptyJar) {
//                    classifier = "javadoc"
//                }
//            }
//            else {
//                publication.artifact(emptyJar) {
//                    classifier = "javadoc"
//                }
//                publication.artifact(emptyJar) {
//                    classifier = "kdoc"
//                }
//            }
//
//            if (target.platformType.name == "native") {
//                publication.artifact(emptyJar)
//            }
//        }
//    }
//
//    // We share emptyJar artifact between all publications, so all publish tasks should be run after all sign tasks.
//    // Otherwise Gradle will throw an error like:
//    //   Task ':publishX' uses output of task ':signY' without declaring an explicit or implicit dependency.
//    tasks.withType<AbstractPublishToMaven>().configureEach { mustRunAfter(tasks.withType<Sign>()) }
//}
//
//// Extension accessors
//private val Project.publishing: PublishingExtension get() = extensions.getByName<PublishingExtension>("publishing")
//private fun Project.publishing(block: PublishingExtension.() -> Unit) = extensions.configure("publishing", block)
//private fun Project.signing(configure: SigningExtension.() -> Unit) = extensions.configure("signing", configure)
//
