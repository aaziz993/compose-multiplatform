package klib.data.database

import klib.data.type.collections.list.whileIndexed
import kotlin.reflect.KClass
import org.reflections.Reflections
import org.reflections.scanners.Scanners.SubTypes
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

public fun <T : Any> getTables(vararg packages: String, tableClass: KClass<T>): List<T> {
    val reflections = Reflections(
        ConfigurationBuilder().forPackages(*packages)
            .filterInputsBy(
                FilterBuilder().apply {
                    packages.forEach(::includePackage)
                },
            )
            .addScanners(SubTypes),
    )

    return reflections.getSubTypesOf<T>(tableClass.java).map { clazz -> clazz.kotlin.objectInstance as T }
}

public fun <T : Any> List<T>.sortedByFkReferences(fkReferenceTables: T.(List<T>) -> List<T>): List<T> {
    val (tables, dependantTables) = map { table -> table to table.fkReferenceTables(this).toMutableList() }
        .partition { (_, referencedTables) -> referencedTables.isEmpty() }
        .let { it.first.map(Pair<T, *>::first).toMutableList() to it.second }

    tables.whileIndexed { _, table ->
        dependantTables.forEach { (dependantTable, dependencies) ->
            if (dependencies.remove(table) && dependencies.isEmpty()) tables.add(dependantTable)
        }
    }

    check(tables.size == size) { "Circular dependency detected among tables!" }

    return tables
}
