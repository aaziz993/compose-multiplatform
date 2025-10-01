package klib.data.crud.model.entity

public interface Entity<ID : Comparable<ID>> {

    public val id: ID?
}
