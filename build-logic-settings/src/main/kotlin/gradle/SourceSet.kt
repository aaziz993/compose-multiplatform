package gradle

import com.android.build.api.dsl.AndroidSourceDirectorySet
import org.gradle.api.file.SourceDirectorySet

internal fun SourceDirectorySet.replace(oldPath: String, newPath: String) =
    setSrcDirs(srcDirs.filterNot { file -> file.endsWith(oldPath) } + newPath)

@Suppress("UnstableApiUsage")
internal fun AndroidSourceDirectorySet.replace(oldPath: String, newPath: String) =
    setSrcDirs(directories.filterNot { file -> file.endsWith(oldPath) } + newPath)
