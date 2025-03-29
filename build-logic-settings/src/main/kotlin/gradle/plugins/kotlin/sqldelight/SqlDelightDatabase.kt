package gradle.plugins.kotlin.sqldelight

import gradle.api.NamedObjectTransformingSerializer
import gradle.api.ProjectNamed
import gradle.api.tryAssign
import gradle.api.trySetFrom
import gradle.plugins.project.Dependency
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KeepGeneratedSerializer

import kotlinx.serialization.Serializable
import org.gradle.api.Project

@KeepGeneratedSerializer
@Serializable(with = SqlDelightDatabaseObjectTransformingSerializer::class)
internal data class SqlDelightDatabase(
    //Note: Name of your Database and .sq file should be same
    override val name: String,
    val packageName: String? = null,
    val schemaOutputDirectory: String? = null,
    val srcDirs: Set<String>? = null,
    val setSrcDirs: Set<String>? = null,
    val deriveSchemaFromMigrations: Boolean? = null,
    val verifyMigrations: Boolean? = null,
    val verifyDefinitions: Boolean? = null,
    val migrationOutputDirectory: String? = null,
    val migrationOutputFileFormat: String? = null,
    val generateAsync: Boolean? = null,
    val modules: Set<Dependency>? = null,
    val dialects: Set<String>? = null,
    /**
     * When SqlDelight finds an equality operation with a nullable typed rvalue such as:
     *
     * ```
     * SELECT *
     * FROM test_table
     * WHERE foo = ?
     * ```
     *
     * It will generate:
     *
     * ```
     * private val foo: String?
     *
     * |SELECT *
     * |FROM test_table
     * |WHERE foo ${ if (foo == null) "IS" else "=" } ?1
     * ```
     *
     * The = operator is expected to return `false` if comparing to a value that is `null`.
     * However, the above code will return true when `foo` is `null`.
     *
     * By enabling [treatNullAsUnknownForEquality], the `null`
     * check will not be generated, resulting in correct SQL behavior:
     *
     * ```
     * private val foo: String?
     *
     * |SELECT *
     * |FROM test_table
     * |WHERE foo = ?1
     * ```
     *
     * @see <a href="https://github.com/cashapp/sqldelight/issues/1490">sqldelight#1490</a>
     * @see <a href="https://en.wikipedia.org/wiki/Null_%28SQL%29#Null-specific_and_3VL-specific_comparison_predicates">Wikipedia entry on null specific comparisons in SQL</a>
     */
    val treatNullAsUnknownForEquality: Boolean? = null,
) : ProjectNamed<app.cash.sqldelight.gradle.SqlDelightDatabase> {

    context(Project)
    override fun applyTo(receiver: app.cash.sqldelight.gradle.SqlDelightDatabase) {
        receiver.packageName tryAssign packageName
        receiver.schemaOutputDirectory tryAssign schemaOutputDirectory?.let(project.layout.projectDirectory::dir)
        srcDirs?.toTypedArray()?.let(receiver::srcDirs)
        receiver.srcDirs trySetFrom setSrcDirs
        receiver.deriveSchemaFromMigrations tryAssign deriveSchemaFromMigrations
        receiver.verifyMigrations tryAssign verifyMigrations
        receiver.verifyDefinitions tryAssign verifyDefinitions
        receiver.migrationOutputDirectory tryAssign migrationOutputDirectory?.let(project.layout.projectDirectory::dir)
        receiver.migrationOutputFileFormat tryAssign migrationOutputFileFormat
        receiver.generateAsync tryAssign generateAsync
        modules?.map { module -> module.resolve() }?.forEach(receiver::module)
        dialects?.forEach(receiver::dialect)
        receiver.treatNullAsUnknownForEquality tryAssign treatNullAsUnknownForEquality
    }
}

private object SqlDelightDatabaseObjectTransformingSerializer
    : NamedObjectTransformingSerializer<SqlDelightDatabase>(SqlDelightDatabase.generatedSerializer())
