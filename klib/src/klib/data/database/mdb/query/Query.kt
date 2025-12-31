package klib.data.database.mdb.query

/**
 * Base interface for classes which encapsulate information about an Access
 * query.  The [.toSQLString] method can be used to convert this
 * object into the actual SQL string which this query data represents.
 *
 * @author Aziz Atoev
 */
public interface Query {
    public enum class Type {
        SELECT,
        MAKE_TABLE,
        APPEND,
        UPDATE,
        DELETE,
        CROSS_TAB,
        DATA_DEFINITION,
        PASSTHROUGH,
        UNION,
        UNKNOWN,
    }

    /**
     * Returns the name of the query.
     */
    public val name: String

    /**
     * Returns the type of the query.
     */
    public val type: Type

    /**
     * Whether or not this query has been marked as hidden.
     * @usage _general_method_
     */
    public val isHidden: Boolean

    /**
     * Returns the unique object id of the query.
     */
    public val objectId: Int

    public val objectFlag: Int

    /**
     * Returns the rows from the system query table from which the query
     * information was derived.
     */
    // public List<Row> getRows();
    public val parameters: List<String>

    public val ownerAccessType: String

    /**
     * Returns the actual SQL string which this query data represents.
     */
    public fun toSQLString(): String
}
