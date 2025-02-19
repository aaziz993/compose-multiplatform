package plugin.project.kotlin.apollo.model

internal interface ApolloExtension {

    val processors: List<ApolloKspProcessor>?
    val generateSourcesDuringGradleSync: Boolean?
    val linkSqlite: Boolean?
}
