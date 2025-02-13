@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
package plugin.project.amperlike.model

import org.jetbrains.amper.frontend.api.SchemaNode

internal sealed class AmperLikeBase : SchemaNode() {
//    @SchemaDoc("The list of repositories used to look up and download the Module dependencies. [Read more](#managing-maven-repositories)")
//    var repositories by nullableValue<List<Repository>>()
//
//    @ModifierAware
//    @SchemaDoc("The list of modules and libraries necessary to build the Module. [Read more](#dependencies)")
//    var dependencies by nullableValue<Map<Modifiers, List<Dependency>>>().provideDelegate()
//
//    @ModifierAware
//    @SchemaDoc("Configures the toolchains used in the build process. [Read more](#settings)")
//    var settings by value(mapOf(noModifiers to Settings())).provideDelegate()
//
//    @ModifierAware
//    @SchemaDoc("The dependencies necessary to build and run tests of the Module. [Read more](#dependencies)")
//    var `test-dependencies` by nullableValue<Map<Modifiers, List<Dependency>>>().provideDelegate()
//
//    @ModifierAware
//    @SchemaDoc("Configures the toolchains used in the build process of the module's tests. [Read more](#settings)")
//    var `test-settings` by value(mapOf(noModifiers to Settings())).provideDelegate()
//
//    @SchemaDoc("Tasks settings. Experimental and will be replaced")
//    var tasks by nullableValue<Map<String, TaskSettings>>().provideDelegate()
}

internal class AmperLikeTemplate : AmperLikeBase()
