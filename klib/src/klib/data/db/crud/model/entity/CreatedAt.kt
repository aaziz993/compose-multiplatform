package klib.data.db.crud.model.entity

public interface CreatedAt<T : Comparable<T>> {

    public val createdAt: T?
}
