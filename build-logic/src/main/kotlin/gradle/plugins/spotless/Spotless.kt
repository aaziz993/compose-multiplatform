package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import gradle.api.project.spotless
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import org.gradle.api.Project

public const val JS_LICENSE_HEADER_DELIMITER: String = """(^("|')use (strict|client)("|'))|(import|export|class|function|interface|type|enum|namespace|module) """

@Suppress("UnusedReceiverParameter", "UNCHECKED_CAST")
context(project: Project)
public fun SpotlessExtension.formats(block: FormatExtension.() -> Unit): Unit =
    project.spotless::class.memberProperties
        .single { property -> property.name == "formats" }
        .let { property ->
            property.isAccessible = true
            property.call(project.spotless) as Map<String, FormatExtension>
        }.values.forEach(block)
