package klib.data.entity

import klib.data.entity.annotation.CreatedAt

public interface CreatedAt<T : Comparable<T>> {
    @CreatedAt
    public val createdAt: T?
}
