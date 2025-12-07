package klib.data.location.locale.weblate.model

public interface WeblateResponse<T> {

     val count: Int
     val next: String?
     val previous: String?
     val results: Set<T>
}
