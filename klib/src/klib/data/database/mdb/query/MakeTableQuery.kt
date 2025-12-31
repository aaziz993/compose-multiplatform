package klib.data.database.mdb.query


/**
 * Query interface which represents an table creation query, e.g.:
 * `SELECT <query> INTO <newTable>`
 *
 * @author James Ahlborn
 */
public interface MakeTableQuery : BaseSelectQuery {
    public val targetTable: String

    public val remoteDbPath: String

    public val remoteDbType: String
}
