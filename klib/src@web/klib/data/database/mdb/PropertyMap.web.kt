package klib.data.database.mdb

public actual class PropertyMap public actual constructor(
    name: String,
    type: Short,
    owner: Table
) : Iterable<Property> {
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val size: Int
        get() = TODO("Not yet implemented")
    public actual val isEmpty: Boolean
        get() = TODO("Not yet implemented")

    public actual fun get(name: String): Property {
        TODO("Not yet implemented")
    }

    public actual fun getValue(name: String): Any {
        TODO("Not yet implemented")
    }

    public actual fun getValue(name: String, defaultValue: Any): Any {
        TODO("Not yet implemented")
    }

    public actual fun put(name: String, value: Any): Property {
        TODO("Not yet implemented")
    }

    public actual fun put(
        name: String,
        type: DataType,
        value: Any
    ): Property {
        TODO("Not yet implemented")
    }

    public actual fun put(
        name: String,
        type: DataType,
        value: Any,
        isDdl: Boolean
    ): Property {
        TODO("Not yet implemented")
    }

    public actual fun putAll(props: Iterable<Property>) {
    }

    public actual fun remove(name: String): Property {
        TODO("Not yet implemented")
    }

    public actual fun save() {
    }

    public actual override operator fun iterator(): Iterator<Property> {
        TODO("Not yet implemented")
    }

    public actual companion object
}

public actual class Property public actual constructor(
    name: String,
    type: DataType,
    value: Any,
    isDdl: Boolean
) {
    public actual val name: String
        get() = TODO("Not yet implemented")
    public actual val type: DataType
        get() = TODO("Not yet implemented")
    public actual val isDdl: Boolean
        get() = TODO("Not yet implemented")
    public actual var value: Any
        get() = TODO("Not yet implemented")
        set(value) {}
}
