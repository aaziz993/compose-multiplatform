package klib.data.db.exposed.column

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.TextColumnType
import org.jetbrains.exposed.v1.core.statements.api.PreparedStatementApi
import org.postgresql.util.PGobject

public class HStoreColumnType : TextColumnType() {

    override fun sqlType(): String = "HSTORE"

    override fun setParameter(stmt: PreparedStatementApi, index: Int, value: Any?) {
        val parameterValue: PGobject? = value?.let {
            PGobject().apply {
                type = sqlType()
                this.value = value as? String
            }
        }
        super.setParameter(stmt, index, parameterValue)
    }
}

public fun Table.hstore(name: String): Column<String> = registerColumn(name, HStoreColumnType())
