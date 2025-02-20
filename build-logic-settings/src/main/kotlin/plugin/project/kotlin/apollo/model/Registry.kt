package plugin.project.kotlin.apollo.model

import kotlinx.serialization.Serializable

@Serializable
inernal data class Registry (
    val graph: org.gradle.api.provider.Property<kotlin.String>

    val graphVariant: org.gradle.api.provider.Property<kotlin.String>

    val key: org.gradle.api.provider.Property<kotlin.String>

    val schemaFile: org.gradle.api.file.RegularFileProperty

    fun schemaConnection(connection: org.gradle.api.Action<com.apollographql.apollo3.gradle.api.SchemaConnection>): kotlin.Unit
)
