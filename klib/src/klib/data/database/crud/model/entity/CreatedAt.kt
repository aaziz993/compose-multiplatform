package klib.data.database.crud.model.entity

public interface CreatedAt<T : Comparable<T>> {

    public val createdAt: T?
}
