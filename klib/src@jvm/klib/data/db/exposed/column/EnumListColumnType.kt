package klib.data.db.exposed.column

import kotlin.reflect.KClass
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.ColumnTransformer
import org.jetbrains.exposed.v1.core.Table

public class EnumListColumnType<T : Enum<T>>(
    private val enumClass: KClass<T>
) : ColumnTransformer<String, List<T>> {

    private val enumConstants by lazy {
        enumClass.java.enumConstants?.associateBy { it.name } ?: emptyMap()
    }

    override fun unwrap(value: List<T>): String {
        return value.joinToString(separator = ",") { it.name }
    }

    override fun wrap(value: String): List<T> = value
        .takeUnless { it.isEmpty() }?.let {
            it.split(',').map { e ->
                enumConstants[e]
                    ?: error("$it can't be associated with any value from ${enumClass.qualifiedName}")
            }
        }
        ?: emptyList()
}

public inline fun <reified T : Enum<T>> Table.enumList(column: Column<String>) = column.transform(EnumListColumnType(T::class))
