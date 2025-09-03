package gradle.plugins.spotless

import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension

context(project: Project)
public fun SpotlessExtension.formats(block: FormatExtension.() -> Unit): Unit {

}