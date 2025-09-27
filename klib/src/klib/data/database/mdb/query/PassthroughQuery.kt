package klib.data.database.mdb.query


/**
 * Query interface which represents a query which will be executed via ODBC.
 *
 * @author James Ahlborn
 */
public interface PassthroughQuery : Query {
    public val connectionString: String

    public val passthroughString: String
}
