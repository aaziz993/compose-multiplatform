package gradle

import gradle.api.catalog.VersionCatalog
import gradle.accessors.env
import gradle.accessors.localProperties
import gradle.accessors.publishing
import gradle.api.getByNameOrAll
import klib.data.type.collection.singleOrAll
import klib.data.type.function.func1
import klib.data.type.get
import klib.data.type.reflection.callMember
import klib.data.type.reflection.memberGetter
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.component.SoftwareComponentContainer
import org.gradle.api.initialization.Settings
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.extra
import org.jetbrains.kotlin.gradle.plugin.extraProperties

@Suppress("FunctionName", "UNCHECKED_CAST")
private fun <T : Any> T._get(key: String): Any? = when (this) {
    is Settings -> when (key) {
        "env" -> env
        "providers" -> providers
        "extra" -> extra
        "extraProperties" -> extraProperties
        "localProperties" -> localProperties
        else -> memberGetter(key)
    }

    is Project -> when (key) {
        "env" -> env
        "providers" -> providers
        "extra" -> extra
        "extraProperties" -> extraProperties
        "localProperties" -> localProperties
        "components" -> components
        "configurations" -> configurations
        "publishing" -> publishing
        "project" -> func1<String, Project> { project(it) }
        else -> memberGetter(key)
    }

    is ProviderFactory -> when (key) {
        "gradleProperty" -> func1<String, Any> { gradleProperty(it) }
        else -> memberGetter(key)
    }

    is Provider<*> -> when (key) {
        "get" -> get()
        else -> memberGetter(key)
    }

    is ExtraPropertiesExtension -> memberGetter(key)

    is SoftwareComponentContainer -> getByNameOrAll(key)

    is ConfigurationContainer -> getByNameOrAll(key).singleOrAll()

    is Configuration -> when (key) {
        "allArtifacts" -> allArtifacts
        else -> memberGetter(key)
    }

    is PublishingExtension -> when (key) {
        "publications" -> publications
        else -> memberGetter(key)
    }

    is PublicationContainer -> getByNameOrAll(key).singleOrAll()

    is DependencySubstitutions -> when (key) {
        "project" -> ::project
        "module" -> ::module
        else -> memberGetter(key)
    }

    is VersionCatalog -> when (key) {
        "versions" -> versions
        "libraries" -> libraries
        "plugins" -> plugins
        else -> memberGetter(key)
    }

    else -> get(key, ::memberGetter)
}

public fun Any.get(vararg keys: String): Any? =
    get(*keys, ::_get)
