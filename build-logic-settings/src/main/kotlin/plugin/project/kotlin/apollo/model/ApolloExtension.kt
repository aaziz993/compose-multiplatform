package plugin.project.kotlin.apollo.model

internal interface ApolloExtension {

    val generateSourcesDuringGradleSync: Boolean?
    val linkSqlite: Boolean?
    val processors: List<ApolloKspProcessor>?
    val androidServices: List<AndroidService>?
    val kotlinService: List<KotlinService>?
    val services: List<Service>?
}
