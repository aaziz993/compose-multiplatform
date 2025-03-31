package gradle.collection

import gradle.accessors.publishing
import gradle.act
import gradle.api.getByNameOrAll
import gradle.reflect.get
import java.util.*
import net.pearx.kasechange.CaseFormat
import net.pearx.kasechange.formatter.format
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.DependencySubstitutions
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.publish.PublicationContainer
import org.gradle.api.publish.PublishingExtension

public infix fun <E> MutableCollection<E>.tryAddAll(value: Iterable<E>?): Boolean? =
    value?.let(::addAll)

public infix fun <E> MutableCollection<E>.trySet(value: Iterable<E>?): Boolean? =
    tryAddAll(value?.act(::clear))
