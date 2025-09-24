package gradle.plugins.kotlin.mpp

import gradle.api.file.replace
import gradle.api.project.sourceSetsToComposeResourcesDirs
import klib.data.type.collections.associateWithNotNull
import klib.data.type.pair
import klib.data.type.primitives.string.lowercaseFirstChar
import klib.data.type.primitives.string.uppercaseFirstChar
import klib.data.type.tuples.and
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinMetadataTarget

private val KOTLIN_COMPILATIONS = listOf(
    KotlinCompilation.MAIN_COMPILATION_NAME,
    KotlinCompilation.TEST_COMPILATION_NAME,
)

private val ANDROID_APPLICATION_COMPILATIONS = KOTLIN_COMPILATIONS + listOf(
    "unitTest",
    "instrumentedTest",
)

context(project: Project)
public fun KotlinMultiplatformExtension.flatten(targetDelimiter: String = "@") {
    val sourceNameParts = targets.flatMap { target ->
        target.compilations.map { compilation ->
            target to compilation.name
        }
    } + targets.filterIsInstance<KotlinMetadataTarget>().map { target ->
        target to KotlinCompilation.TEST_COMPILATION_NAME
    } + targets.filterIsInstance<KotlinAndroidTarget>().flatMap { target ->
        ANDROID_APPLICATION_COMPILATIONS.map { compilationName ->
            target to compilationName
        }
    }

    val sourceSets = sourceSets.associateWithNotNull { sourceSet ->
        sourceNameParts.find { (target, compilationName) ->
            sourceSet.name == "${
                if (target is KotlinMetadataTarget) "common" else target.targetName
            }${compilationName.uppercaseFirstChar()}"
        }?.let { (target, compilationName) ->
            (if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
            else compilationName.pair()) and if (target is KotlinMetadataTarget) ""
            else "${targetDelimiter}${target.targetName}"
        }
    }

    val customSourceSets = (this.sourceSets - sourceSets.keys).associateWithNotNull { sourceSet ->
        KOTLIN_COMPILATIONS.find { compilationName ->
            sourceSet.name.endsWith(compilationName.uppercaseFirstChar())
        }?.let { compilationName ->
            (if (compilationName == KotlinCompilation.MAIN_COMPILATION_NAME) "src" to ""
            else compilationName.pair()) and "${targetDelimiter}${
                sourceSet.name.removeSuffix(compilationName.uppercaseFirstChar())
            }"
        }
    }

    (sourceSets + customSourceSets).forEach { (sourceSet, parts) ->
        val (srcPart, resourcesPart, targetPart) = parts

        sourceSet.kotlin.replace("src/${sourceSet.name}/kotlin", "$srcPart$targetPart")
        sourceSet.resources.replace(
            "src/${sourceSet.name}/resources",
            "${resourcesPart}Resources$targetPart".lowercaseFirstChar(),
        )
        project.sourceSetsToComposeResourcesDirs[sourceSet] = project.layout.projectDirectory.dir(
            "${resourcesPart}ComposeResources$targetPart".lowercaseFirstChar(),
        )
    }
}
