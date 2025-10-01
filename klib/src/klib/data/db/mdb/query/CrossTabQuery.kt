package klib.data.db.mdb.query

/**
 * Query interface which represents a crosstab/pivot query, e.g.:
 * `TRANSFORM <expr> SELECT <query> PIVOT <expr>`
 *
 * @author Aziz Atoev
 */
public interface CrossTabQuery : BaseSelectQuery {
    public val transformExpression: String

    public val pivotExpression: String
}
