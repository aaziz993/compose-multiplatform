package gradle.project.file

import arrow.core.fold
import gradle.isUrl
import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlinx.serialization.Serializable
import org.apache.commons.io.FileUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.AbstractCopyTask
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.register
import org.jetbrains.compose.internal.de.undercouch.gradle.tasks.download.Download

internal interface ProjectFile {

    val from: String
    val into: String
    val resolution: FileResolution
    val replace: Map<String, String>

    context(Project)
    fun applyTo(name: String) =
        if (from.isUrl) {
            rootProject.tasks.register<Download>(name) {
                src(from)
                dest(into)

                when (resolution) {
                    FileResolution.ABSENT -> overwrite(false)
                    FileResolution.OVERRIDE -> overwrite(true)
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
                        dest.writeText(transform?.invoke(content) ?: content)
                    }
                }
            }
        }
        else {
            rootProject.tasks.register<Copy>(name) {
                from(from)
                into(into)
                when (resolution) {
                    FileResolution.ABSENT -> duplicatesStrategy = DuplicatesStrategy.EXCLUDE
                    FileResolution.OVERRIDE -> duplicatesStrategy = DuplicatesStrategy.INCLUDE
                    FileResolution.MODIFIED -> excludeFile(FileUtils::contentEquals)
                    FileResolution.NEWER -> excludeFile { fromFile, intoFile ->
                        fromFile.lastModified() > intoFile.lastModified()
                    }
                }
                filter(replace, ReplaceTokens::class.java)

                includeEmptyDirs = false
            }
        }

    context(Project)
    private fun AbstractCopyTask.excludeFile(equator: (from: File, into: File) -> Boolean) =
        exclude { fileTree ->
            if (fileTree.isDirectory) return@exclude false

            val intoFile = file(into).let { intoFile ->
                if (intoFile.isDirectory()) intoFile.resolve(fileTree.relativePath.pathString)
                else intoFile
            }
            equator(fileTree.file, intoFile)
        }
}
