package gradle.plugins.project.file

import arrow.core.fold
import gradle.isValidUrl
import java.io.File
import java.net.URI
import org.apache.commons.io.FileUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskProvider
import org.gradle.internal.impldep.org.apache.ivy.util.url.ApacheURLLister
import org.gradle.kotlin.dsl.register
import org.jetbrains.compose.internal.de.undercouch.gradle.tasks.download.Download

internal interface ProjectFile {

    val from: List<String>
    val into: String
    val resolution: FileResolution

    val replace: Map<String, String>
        get() = emptyMap()

    context(Project)
    fun applyTo(receiver: String): List<TaskProvider<out DefaultTask>> {
        val (urls, files) = from.partition(String::isValidUrl)

        return listOfNotNull(
            urls.takeIf(List<*>::isNotEmpty)?.flatMap { url ->
                if (url.endsWith("/")) {
                    val urlLister = ApacheURLLister()
                    urlLister.listFiles(URI(url).toURL())
                }
                else listOf(url)
            }.let { urls ->
                project.rootProject.tasks.register<Download>(receiver) {
                    doLast {
                        try {
                            download().run {
                                src(urls)
                                dest(into)

                                when (resolution) {
                                    FileResolution.ABSENT -> overwrite(false)
                                    FileResolution.OVERRIDE -> {
                                        overwrite(true)
                                        tempAndMove(true)
                                    }

                                    FileResolution.MODIFIED -> onlyIfModified(true)
                                    FileResolution.NEWER -> onlyIfNewer(true)
                                }

                                doLast {
                                    if (dest.exists()) {
                                        // Read the downloaded file and replace placeholders
                                        val content = replace.fold(dest.readText()) { acc, (key, value) ->
                                            acc.replace(key, value)
                                        }

                                        // Write the modified content back to the file
                                        dest.writeText(content)
                                    }
                                }
                            }
                        }
                        catch (e: Exception) {
                            logger.warn(e.message!!)
                        }
                    }
                }
            },
            files.takeIf(List<*>::isNotEmpty)?.let { files ->
                project.rootProject.tasks.register<Copy>(receiver) {
                    from(*files.toTypedArray())
                    into(into)
                    when (resolution) {
                        FileResolution.ABSENT -> duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                        FileResolution.OVERRIDE -> duplicatesStrategy = DuplicatesStrategy.INCLUDE
                        FileResolution.MODIFIED -> exclude(FileUtils::contentEquals)
                        FileResolution.NEWER -> exclude { fromFile, intoFile ->
                            fromFile.lastModified() > intoFile.lastModified()
                        }
                    }
                    filter(replace, ReplaceTokens::class.java)

                    includeEmptyDirs = false
                }
            },
        )
    }

    context(Project)
    fun AbstractCopyTask.exclude(equator: (from: File, into: File) -> Boolean) =
        exclude { fileTree ->
            if (fileTree.isDirectory) false
            else {
                val intoFile = project.file(into).let { intoFile ->
                    if (intoFile.isDirectory()) intoFile.resolve(fileTree.relativePath.pathString)
                    else intoFile
                }
                equator(fileTree.file, intoFile)
            }
        }
}
