package gradle.plugins.apollo

import com.apollographql.apollo3.gradle.api.ApolloExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.the

public val Project.apollo: ApolloExtension get() = the()

public fun Project.apollo(configure: ApolloExtension.() -> Unit): Unit = extensions.configure(configure)

