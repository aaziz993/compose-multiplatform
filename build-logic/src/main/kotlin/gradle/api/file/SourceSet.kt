package gradle.api.file

import com.android.build.api.dsl.AndroidSourceDirectorySet
import org.gradle.api.file.SourceDirectorySet

public fun SourceDirectorySet.remove(vararg paths: String) =
    setSrcDirs(srcDirs.filterNot { file -> paths.any(file::endsWith) })

public fun SourceDirectorySet.add(vararg paths: String) = srcDirs(*paths)

public fun SourceDirectorySet.replace(oldPath: String, newPath: String) = remove(oldPath).add(newPath)

@Suppress("UnstableApiUsage")
public fun AndroidSourceDirectorySet.replace(oldPath: String, newPath: String) =
    setSrcDirs(directories.filterNot { file -> file.endsWith(oldPath) } + newPath)
