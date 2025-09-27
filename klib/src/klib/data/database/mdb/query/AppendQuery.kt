package klib.data.database.mdb.query

/**
 * Query interface which represents an append query, e.g.:
 * `INSERT INTO <table> VALUES (<values>)`
 *
 * @author Aziz Atoev
 */
public interface AppendQuery : BaseSelectQuery {
    public val targetTable: String

    public val targetColumns: List<String>

    public val remoteDbPath: String

    public val remoteDbType: String

    public val values: List<String>
}