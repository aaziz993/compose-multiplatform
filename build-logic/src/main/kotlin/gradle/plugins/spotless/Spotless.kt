package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import gradle.api.project.spotless
import org.gradle.api.Project
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

context(project: Project)
public fun SpotlessExtension.formats(block: FormatExtension.() -> Unit): Unit =
    project.spotless::class.memberProperties
        .single { property -> property.name == "formats" }
        .let { property ->
            property.isAccessible = true
            property.call(project.spotless) as Map<String, FormatExtension>
        }.values.forEach(block)