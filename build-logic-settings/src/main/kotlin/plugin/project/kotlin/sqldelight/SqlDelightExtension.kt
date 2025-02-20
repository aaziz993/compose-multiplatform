package plugin.project.kotlin.sqldelight

import app.cash.sqldelight.gradle.SqlDelightPlugin
import gradle.moduleProperties
import gradle.sqldelight
import gradle.tryAssign
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.gradle.kotlin.dsl.invoke
import plugin.model.dependency.DependencyNotation

internal fun Project.configureSqlDelightExtension() =
    plugins.withType<SqlDelightPlugin> {
        moduleProperties.settings.kotlin.sqldelight.let { sqldelight ->
            sqldelight {
                sqldelight.databases?.let { databases ->
                    databases {
                        databases.forEach { database ->
                            //Note: Name of your Database and .sq file should be same
                            create(database.name) {
                                packageName tryAssign database.packageName
                                schemaOutputDirectory tryAssign database.schemaOutputDirectory?.let(layout.projectDirectory::dir)
                                database.srcDirs?.let(srcDirs::setFrom)
                                deriveSchemaFromMigrations tryAssign database.deriveSchemaFromMigrations
                                verifyMigrations tryAssign database.verifyMigrations
                                verifyDefinitions tryAssign database.verifyDefinitions
                                migrationOutputDirectory tryAssign database.migrationOutputDirectory?.let(layout.projectDirectory::dir)
                                migrationOutputFileFormat tryAssign database.migrationOutputFileFormat
                                generateAsync tryAssign database.generateAsync
                                database.modules?.map { it.toDependencyNotation() }?.forEach(::module)
                                database.dialect?.let(::dialect)
                                treatNullAsUnknownForEquality tryAssign database.treatNullAsUnknownForEquality
                            }
                        }
                    }
                }
                linkSqlite tryAssign sqldelight.linkSqlite
            }
        }
    }
