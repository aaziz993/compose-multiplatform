/*
Copyright (c) 2013 James Ahlborn

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package klib.data.database.mdb

import klib.data.database.mdb.complex.ComplexColumnInfo
import klib.data.database.mdb.complex.ComplexValue
import kotlinx.io.IOException

/**
 * Access database column definition.  A [com.healthmarketscience.jackcess.Table] has a list of Column
 * instances describing the table schema.
 *
 *
 * A Column instance is not thread-safe (see [com.healthmarketscience.jackcess.Database] for more
 * thread-safety details).
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public expect class Column(
    name: String,
    type: DataType,
    table: Table,
) {

    public constructor(
        name: String,
        type: DataType,
        tableDefinition: TableDefinition
    )

    /**
     * @usage _general_method_
     */
    public val table: Table

    /**
     * @usage _general_method_
     */
    public val database: Database

    /**
     * @usage _general_method_
     */
    public val name: String

    /**
     * @usage _advanced_method_
     */
    public val isVariableLength: Boolean

    /**
     * @usage _general_method_
     */
    public val isAutoNumber: Boolean

    /**
     * @usage _advanced_method_
     */
    public val columnIndex: Int

    /**
     * @usage _general_method_
     */
    public val type: DataType

    /**
     * @usage _general_method_
     */
    public val isCompressedUnicode: Boolean

    /**
     * @usage _general_method_
     */
    public val precision: Byte

    /**
     * @usage _general_method_
     */
    public val scale: Byte

    /**
     * @usage _general_method_
     */
    public val length: Short

    /**
     * @usage _general_method_
     */
    public val lengthInUnits: Short

    /**
     * Whether or not this column is "append only" (its history is tracked by a
     * separate version history column).
     * @usage _general_method_
     */
    public val isAppendOnly: Boolean

    /**
     * Returns whether or not this is a hyperlink column (only possible for
     * columns of type MEMO).
     * @usage _general_method_
     */
    public val isHyperlink: Boolean

    /**
     * Returns whether or not this is a calculated column.  Note that jackess
     * **won't interpret the calculation expression** (but the field can be
     * written directly).
     * @usage _general_method_
     */
    public val isCalculated: Boolean

    /**
     * Returns extended functionality for "complex" columns.
     * @usage _general_method_
     */
    public val complexInfo: ComplexColumnInfo<out ComplexValue>?

    /**
     * @return the properties for this column
     * @usage _general_method_
     */
    public val properties: PropertyMap
        @Throws(IOException::class)
        get

    /**
     * Returns the column which tracks the version history for an "append only"
     * column.
     * @usage _intermediate_method_
     */
    public val versionHistoryColumn: Column

    /**
     * ColumnValidator.  If `null`, resets to the value
     * returned from the Database's ColumnValidatorFactory (if the factory
     * returns `null`, then the default is used).  Autonumber columns
     * cannot have a validator instance other than the default.
     * @throws IllegalArgumentException if an attempt is made to set a
     * non-`null` ColumnValidator instance on an autonumber column
     * @usage _intermediate_method_
     */
    public var columnValidator: ColumnValidator?

    public fun setRowValue(rowArray: Array<Any?>, value: Any?): Any?

    public fun setRowValue(rowMap: Map<String, Any?>, value: Any?): Any?

    public fun getRowValue(rowArray: Array<Any?>): Any?

    public fun getRowValue(rowMap: Map<String, *>): Any?
}
