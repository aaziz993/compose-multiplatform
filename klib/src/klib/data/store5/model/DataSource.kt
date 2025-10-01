package klib.data.store5.model

import org.mobilenativefoundation.store.store5.StoreReadRequest

public sealed interface DataSource<Key : Any> {

    public fun request(key: Key): StoreReadRequest<Key>

    public data class Fresh<Key : Any>(val fallBackToSourceOfTruth: Boolean = false) : DataSource<Key> {

        override fun request(key: Key): StoreReadRequest<Key> = StoreReadRequest.fresh(key, fallBackToSourceOfTruth)
    }

    public data class Cached<Key : Any>(val refresh: Boolean) : DataSource<Key> {

        override fun request(key: Key): StoreReadRequest<Key> = StoreReadRequest.cached(key, refresh)
    }

    public class LocalOnly<Key : Any> : DataSource<Key> {

        override fun request(key: Key): StoreReadRequest<Key> = StoreReadRequest.localOnly(key)
    }

    public data class SkipMemory<Key : Any>(val refresh: Boolean) : DataSource<Key> {

        override fun request(key: Key): StoreReadRequest<Key> = StoreReadRequest.skipMemory(key, refresh)
    }
}
