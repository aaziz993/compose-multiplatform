package klib.data.database.mdb

import kotlinx.io.IOException

/**
 * Map of properties for a database object.
 *
 * @author Aziz Atoev
 * @usage _general_class_
 */
public expect class PropertyMap(name: String, type: Short, owner: Table) : Iterable<Property> {

    public val name: String

    public val size: Int

    public val isEmpty: Boolean

    /**
     * @return the property with the given name, if any
     */
    public fun get(name: String): Property

    /**
     * @return the value of the property with the given name, if any
     */
    public fun getValue(name: String): Any

    /**
     * @return the value of the property with the given name, if any, otherwise
     * the given defaultValue
     */
    public fun getValue(name: String, defaultValue: Any): Any

    /**
     * Creates a new (or updates an existing) property in the map.  Attempts to
     * determine the type of the property based on the name and value (the
     * property names listed above have their types builtin, otherwise the type
     * of the value is used).
     *
     *
     * Note, this change will not be persisted until the [.save] method
     * has been called.
     *
     * @return the newly created (or updated) property
     * @throws IllegalArgumentException if the type of the property could not be
     * determined automatically
     */
    public fun put(name: String, value: Any): Property

    /**
     * Creates a new (or updates an existing) property in the map.
     *
     *
     * Note, this change will not be persisted until the [.save] method
     * has been called.
     *
     * @return the newly created (or updated) property
     */
    public fun put(name: String, type: DataType, value: Any): Property

    /**
     * Creates a new (or updates an existing) property in the map.
     *
     *
     * Note, this change will not be persisted until the [.save] method
     * has been called.
     *
     * @return the newly created (or updated) property
     */
    public fun put(name: String, type: DataType, value: Any, isDdl: Boolean): Property

    /**
     * Puts all the given properties into this map.
     *
     * @param props the properties to put into this map (`null` is
     * tolerated and ignored).
     */
    public fun putAll(props: Iterable<Property>)

    /**
     * Removes the property with the given name
     *
     * @return the removed property, or `null` if none found
     */
    public fun remove(name: String): Property

    /**
     * Saves the current state of this map.
     */
    @Throws(IOException::class)
    public fun save()
    override fun iterator(): Iterator<Property>

    public companion object
}

/**
 * Info about a property defined in a PropertyMap.
 */
public expect class Property(
    name: String,
    type: DataType,
    value: Any,
    isDdl: Boolean
) {

    public val name: String

    public val type: DataType

    /**
     * Whether or not this property is a DDL object.  If `true`, users
     * can't change or delete the property in access without the dbSecWriteDef
     * permission.  Additionally, certain properties must be flagged correctly
     * or the access engine may not recognize them correctly.
     */
    public val isDdl: Boolean

    public var value: Any
}

public val PropertyMap.Companion.ACCESS_VERSION_PROP: String
    get() = "AccessVersion"

public val PropertyMap.Companion.TITLE_PROP: String
    get() = "Title"

public val PropertyMap.Companion.AUTHOR_PROP: String
    get() = "Author"

public val PropertyMap.Companion.COMPANY_PROP: String
    get() = "Company"

public val PropertyMap.Companion.DEFAULT_VALUE_PROP: String
    get() = "DefaultValue"

public val PropertyMap.Companion.REQUIRED_PROP: String
    get() = "Required"

public val PropertyMap.Companion.ALLOW_ZERO_LEN_PROP: String
    get() = "AllowZeroLength"

public val PropertyMap.Companion.DECIMAL_PLACES_PROP: String
    get() = "DecimalPlaces"

public val PropertyMap.Companion.FORMAT_PROP: String
    get() = "Format"

public val PropertyMap.Companion.INPUT_MASK_PROP: String
    get() = "InputMask"

public val PropertyMap.Companion.CAPTION_PROP: String
    get() = "Caption"
public val PropertyMap.Companion.VALIDATION_RULE_PROP: String
    get() = "ValidationRule"
public val PropertyMap.Companion.VALIDATION_TEXT_PROP: String
    get() = "ValidationText"
public val PropertyMap.Companion.GUID_PROP: String
    get() = "GUID"
public val PropertyMap.Companion.DESCRIPTION_PROP: String
    get() = "Description"
public val PropertyMap.Companion.RESULT_TYPE_PROP: String
    get() = "ResultType"
public val PropertyMap.Companion.EXPRESSION_PROP: String
    get() = "Expression"
public val PropertyMap.Companion.ALLOW_MULTI_VALUE_PROP: String
    get() = "AllowMultipleValues"
public val PropertyMap.Companion.ROW_SOURCE_TYPE_PROP: String
    get() = "RowSourceType"
public val PropertyMap.Companion.ROW_SOURCE_PROP: String
    get() = "RowSource"
public val PropertyMap.Companion.DISPLAY_CONTROL_PROP: String
    get() = "DisplayControl"
public val PropertyMap.Companion.TEXT_FORMAT_PROP: String
    get() = "TextFormat"
public val PropertyMap.Companion.IME_MODE_PROP: String
    get() = "IMEMode"
public val PropertyMap.Companion.IME_SENTENCE_MODE_PROP: String
    get() = "IMESentenceMode"

/**
 * Interface for enums which can be used as property values.
 */
public interface EnumValue {
    /**
     * @return the property value which should be stored in the db
     */
    public val value: Any
}

/**
 * Enum value constants for the DisplayControl property
 */
public enum class DisplayControl(override val value: Any) : EnumValue {
    BOUND_OBJECT_FRAME(108),
    CHECK_BOX(106),
    COMBO_BOX(111),
    COMMAND_BUTTON(104),
    CUSTOM_CONTROL(119),
    EMPTY_CELL(127),
    IMAGE(103),
    LABEL(100),
    LINE(102),
    LIST_BOX(110),
    NAVIGATION_BUTTON(130),
    NAVIGATION_CONTROL(129),
    OBJECT_FRAME(114),
    OPTION_BUTTON(105),
    OPTION_GROUP(107),
    PAGE(124),
    PAGE_BREAK(118),
    RECTANGLE(101),
    SUB_FORM(112),
    TAB_CTL(123),
    TEXT_BOX(109),
    TOGGLE_BUTTON(122),
    WEB_BROWSER(128);
}

/**
 * Enum value constants for the TextFormat property
 */
public enum class TextFormat(override val value: Any) : EnumValue {
    HTMLRICHTEXT(1),
    PLAIN(0);
}

/**
 * Enum value constants for the IMEMode property
 */
public enum class IMEMode(override val value: Any) : EnumValue {
    NOCONTROL(0),
    ON(1),
    OFF(2),
    DISABLE(3),
    HIRAGANA(4),
    KATAKANA(5),
    KATAKANAHALF(6),
    ALPHAFULL(7),
    ALPHA(8),
    HANGULFULL(9),
    HANGUL(10);
}

/**
 * Enum value constants for the IMESentenceMode property
 */
public enum class IMESentenceMode(override val value: Any) : EnumValue {
    NORMAL(0),
    PLURAL(1),
    SPEAKING(2),
    NONE(3);
}
