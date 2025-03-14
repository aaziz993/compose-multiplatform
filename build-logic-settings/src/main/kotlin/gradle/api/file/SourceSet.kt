package gradle.api.file

import com.android.build.api.dsl.AndroidSourceDirectorySet
import org.gradle.api.file.SourceDirectorySet

internal fun SourceDirectorySet.remove(vararg paths: String) =
    setSrcDirs(srcDirs.filterNot { file -> paths.any(file::endsWith) })

internal fun SourceDirectorySet.add(vararg paths: String) =
    srcDirs(*paths)

internal fun SourceDirectorySet.replace(oldPath: String, newPath: String) =
    remove(oldPath).add(newPath)

@Suppress("UnstableApiUsage")
internal fun AndroidSourceDirectorySet.replace(oldPath: String, newPath: String) =
    setSrcDirs(directories.filterNot { file -> file.endsWith(oldPath) } + newPath)
