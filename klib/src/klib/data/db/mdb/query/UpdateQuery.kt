package klib.data.db.mdb.query

/**
 * Query interface which represents a row update query, e.g.:
 * `UPDATE <table> SET <newValues>`
 *
 * @author Aziz Atoev
 */
public interface UpdateQuery : Query {
    public val targetTables: List<String>

    public val remoteDbPath: String

    public val remoteDbType: String

    public val newValues: List<String>

    public val whereExpression: String
}
