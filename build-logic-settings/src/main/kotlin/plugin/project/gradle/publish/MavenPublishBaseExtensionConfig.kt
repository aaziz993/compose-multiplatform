package plugin.project.gradle.publish//package plugin.project.gradle.publish
//
//import com.vanniktech.maven.publish.MavenPublishBaseExtension
//import org.gradle.api.Project
//import org.gradle.accessors.kotlin.dsl.settings
//
//internal fun Project.configureMavenPublishBaseExtension(extension: MavenPublishBaseExtension) = extension.apply {
//    with(settings.extension) {
//        coordinates(group.toString(), name, version.toString())
//
//
//        publishToMavenCentral(
//            when (providers.gradleProperty("sonatype.url").get()) {
//                "https://oss.sonatype.org" -> "DEFAULT"
//                "https://s01.oss.sonatype.org" -> "S01"
//                else -> "CENTRAL_PORTAL"
//            },
//            providers.gradleProperty("sonatype.autopush").get().toBoolean(),
//        )
//
//        // Enable GPG signing for all publications
//        signAllPublications()
//    }
//}
//
