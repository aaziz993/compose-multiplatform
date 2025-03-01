package gradle

import com.android.build.api.dsl.AndroidSourceDirectorySet
import java.io.File
import org.gradle.api.file.SourceDirectorySet
import org.jetbrains.amper.gradle.ReflectionSourceDirectorySet

internal fun SourceDirectorySet.tryAdd(toAdd: Any): SourceDirectorySet {
    val delegate = ReflectionSourceDirectorySet.tryWrap(this)
    if (delegate != null) with(delegate.mutableSources) {
        if (!contains(toAdd)) add(toAdd)
    }
    return this
}

internal fun SourceDirectorySet.tryRemove(toRemove: (File) -> Boolean): SourceDirectorySet {
    val delegate = ReflectionSourceDirectorySet.tryWrap(this)
    if (delegate != null) with(delegate.mutableSources) {
        removeIf { (it as? File)?.let(toRemove) == true }
    }
    return this
}

internal fun SourceDirectorySet.replace(oldPath: String, newPath: String) =
    tryAdd(newPath).tryRemove { file -> file.endsWith(oldPath) }

@Suppress("UnstableApiUsage")
internal fun AndroidSourceDirectorySet.replace(oldPath: String, newPath: String) =
    setSrcDirs(directories.filterNot { file -> file.endsWith(oldPath) } + newPath)
