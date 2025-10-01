package klib.data.db.mdb.query

/**
 * Base interface for queries which represent some form of SELECT statement.
 *
 * @author Aziz Atoev
 */
public interface BaseSelectQuery : Query {
    public val selectType: String

    public val selectColumns: List<String>

    public val fromTables: List<String>

    public val fromRemoteDbPath: String

    public val fromRemoteDbType: String

    public val whereExpression: String

    public val groupings: List<String>

    public val havingExpression: String

    public val orderings: List<String>
}
