package klib.data.database.mdb

import com.healthmarketscience.jackcess.impl.PropertyMapImpl
import com.healthmarketscience.jackcess.impl.TableImpl
import kotlinx.io.IOException

public actual class PropertyMap(public val propertyMap: com.healthmarketscience.jackcess.PropertyMap) :
    Iterable<Property> {

    public actual constructor(name: String, type: Short, owner: Table) :
        this(PropertyMapImpl(name, type, (owner.table as TableImpl).propertyMaps))

    public actual val name: String
        get() = propertyMap.name
    public actual val size: Int
        get() = propertyMap.size

    public actual val isEmpty: Boolean
        get() = propertyMap.isEmpty

    public actual fun get(name: String): Property = Property(propertyMap.get(name))

    public actual fun getValue(name: String): Any = propertyMap.getValue(name)

    public actual fun getValue(name: String, defaultValue: Any): Any = propertyMap.getValue(name, defaultValue)

    public actual fun put(name: String, value: Any): Property = Property(propertyMap.put(name, value))

    public actual fun put(name: String, type: DataType, value: Any): Property =
        Property(propertyMap.put(name, DATA_TYPES.inverse[type], value))

    public actual fun put(name: String, type: DataType, value: Any, isDdl: Boolean): Property =
        Property(propertyMap.put(name, DATA_TYPES.inverse[type], value, isDdl))

    public actual fun putAll(props: Iterable<Property>): Unit = propertyMap.putAll(
        props.map(Property::property),
    )

    public actual fun remove(name: String): Property = Property(propertyMap.remove(name))

    @Throws(IOException::class)
    public actual fun save(): Unit = propertyMap.save()

    actual override fun iterator(): Iterator<Property> =
        propertyMap.iterator().asSequence().map(::Property).iterator()

    public actual companion object
}

public actual class Property(public val property: com.healthmarketscience.jackcess.PropertyMap.Property) {
    public actual constructor(
        name: String,
        type: DataType,
        value: Any,
        isDdl: Boolean
    ) : this(
        PropertyMapImpl.createProperty(
            name,
            DATA_TYPES.inverse[type],
            value,
            isDdl,
        ),
    )

    public actual val name: String
        get() = property.name

    public actual val type: DataType
        get() = DATA_TYPES[property.type]!!

    public actual val isDdl: Boolean
        get() = property.isDdl

    public actual var value: Any
        get() = property.value
        set(value) {
            property.value = value
        }
}
