package plugin.project.kotlin.sqldelight.model

import app.cash.sqldelight.gradle.SqlDelightDatabase
import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import gradle.model.project.Dependency
import gradle.model.project.DependencyTransformingSerializer

@Serializable
internal data class SqlDelightDatabase(
    val name: String,
    val packageName: String? = null,
    val schemaOutputDirectory: String? = null,
    val srcDirs: List<String>? = null,
    val deriveSchemaFromMigrations: Boolean? = null,
    val verifyMigrations: Boolean? = null,
    val verifyDefinitions: Boolean? = null,
    val migrationOutputDirectory: String? = null,
    val migrationOutputFileFormat: String? = null,
    val generateAsync: Boolean? = null,
    val modules: List<@Serializable(with = DependencyTransformingSerializer::class) Dependency>? = null,
    val dialect: String? = null,
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
) {

    context(Project)
    fun applyTo(database: SqlDelightDatabase) {
        database.packageName tryAssign packageName
        database.schemaOutputDirectory tryAssign schemaOutputDirectory?.let(layout.projectDirectory::dir)
        srcDirs?.let(database.srcDirs::setFrom)
        database.deriveSchemaFromMigrations tryAssign deriveSchemaFromMigrations
        database.verifyMigrations tryAssign verifyMigrations
        database.verifyDefinitions tryAssign verifyDefinitions
        database.migrationOutputDirectory tryAssign migrationOutputDirectory?.let(layout.projectDirectory::dir)
        database.migrationOutputFileFormat tryAssign migrationOutputFileFormat
        database.generateAsync tryAssign generateAsync
        modules?.filterIsInstance<Dependency>()?.map { it.resolve() }?.forEach(database::module)
        dialect?.let(database::dialect)
        database.treatNullAsUnknownForEquality tryAssign treatNullAsUnknownForEquality
    }
}
