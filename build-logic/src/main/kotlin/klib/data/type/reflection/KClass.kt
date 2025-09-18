package klib.data.type.reflection

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import klib.data.type.collections.drop
import klib.data.type.collections.list.drop
import klib.data.type.primitives.BIG_DECIMAL_DEFAULT
import klib.data.type.primitives.BIG_INTEGER_DEFAULT
import klib.data.type.primitives.BOOLEAN_DEFAULT
import klib.data.type.primitives.BYTE_DEFAULT
import klib.data.type.primitives.CHAR_DEFAULT
import klib.data.type.primitives.DATE_PERIOD_DEFAULT
import klib.data.type.primitives.DATE_TIME_PERIOD_DEFAULT
import klib.data.type.primitives.DOUBLE_DEFAULT
import klib.data.type.primitives.DURATION_DEFAULT
import klib.data.type.primitives.FLOAT_DEFAULT
import klib.data.type.primitives.INT_DEFAULT
import klib.data.type.primitives.LOCAL_DATE_DEFAULT
import klib.data.type.primitives.LOCAL_DATE_TIME_DEFAULT
import klib.data.type.primitives.LOCAL_TIME_DEFAULT
import klib.data.type.primitives.LONG_DEFAULT
import klib.data.type.primitives.SHORT_DEFAULT
import klib.data.type.primitives.STRING_DEFAULT
import klib.data.type.primitives.UUID_DEFAULT
import klib.data.type.primitives.U_BYTE_DEFAULT
import klib.data.type.primitives.U_INT_DEFAULT
import klib.data.type.primitives.U_LONG_DEFAULT
import klib.data.type.primitives.U_SHORT_DEFAULT
import klib.data.type.primitives.parseOrNull
import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import kotlin.collections.ArrayList
import kotlin.collections.Collection
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.all
import kotlin.collections.any
import kotlin.collections.filter
import kotlin.collections.find
import kotlin.collections.firstOrNull
import kotlin.collections.forEach
import kotlin.collections.map
import kotlin.collections.plus
import kotlin.collections.toList
import kotlin.collections.toTypedArray
import kotlin.collections.zip
import kotlin.reflect.*
import kotlin.reflect.full.*
import kotlin.sequences.Sequence
import kotlin.sequences.filter
import kotlin.sequences.sequence
import kotlin.text.toBooleanStrictOrNull
import kotlin.text.toByteOrNull
import kotlin.text.toDoubleOrNull
import kotlin.text.toFloatOrNull
import kotlin.text.toIntOrNull
import kotlin.text.toLongOrNull
import kotlin.text.toShortOrNull
import kotlin.text.toUByteOrNull
import kotlin.text.toUIntOrNull
import kotlin.text.toULongOrNull
import kotlin.text.toUShortOrNull
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

//////////////////////////////////////////////////////////GENERIC///////////////////////////////////////////////////////
public val KClass<*>.isUIntNumber: Boolean
    get() = when (this) {
        UByte::class, UShort::class, UInt::class, ULong::class -> true
        else -> false
    }

public val KClass<*>.isIntNumber: Boolean
    get() = when (this) {
        Byte::class, Short::class, Int::class, Long::class -> true
        else -> false
    }

public val KClass<*>.isFloatNumber: Boolean
    get() = when (this) {
        Float::class, Double::class -> true
        else -> false
    }

public val KClass<*>.isBigNumber: Boolean
    get() = when (this) {
        BigInteger::class, BigDecimal::class -> true
        else -> false
    }

public val KClass<*>.isNumber: Boolean
    get() = isUIntNumber || isIntNumber || isFloatNumber || isBigNumber

public val KClass<*>.isChar: Boolean
    get() = this == Char::class

public val KClass<*>.isString: Boolean
    get() = this == String::class

public val KClass<*>.isSymbolic: Boolean
    get() = isChar || isString

public val KClass<*>.isTime: Boolean
    get() = when (this) {
        LocalTime::class, LocalDate::class, LocalDateTime::class, Duration::class, DatePeriod::class, DateTimePeriod::class -> true
        else -> false
    }

public val KClass<*>.isPrimitive: Boolean
    get() = isNumber || isSymbolic || isTime || this == Uuid::class

public val KClass<*>.isList: Boolean
    get() = when (this) {
        List::class, MutableList::class, ArrayList::class -> true
        else -> false
    }

public val KClass<*>.isSet: Boolean
    get() = when (this) {
        Set::class, MutableSet::class, HashSet::class, LinkedHashSet::class -> true
        else -> false
    }

public val KClass<*>.isMap: Boolean
    get() = when (this) {
        Map::class, MutableMap::class, HashMap::class, LinkedHashMap::class -> true
        else -> false
    }

public fun KClass<*>.primitiveDefault(
    booleanDefault: Boolean = BOOLEAN_DEFAULT,
    uByteDefault: UByte = U_BYTE_DEFAULT,
    uShortDefault: UShort = U_SHORT_DEFAULT,
    uIntDefault: UInt = U_INT_DEFAULT,
    uLongDefault: ULong = U_LONG_DEFAULT,
    byteDefault: Byte = BYTE_DEFAULT,
    shortDefault: Short = SHORT_DEFAULT,
    intDefault: Int = INT_DEFAULT,
    longDefault: Long = LONG_DEFAULT,
    floatDefault: Float = FLOAT_DEFAULT,
    doubleDefault: Double = DOUBLE_DEFAULT,
    charDefault: Char = CHAR_DEFAULT,
    stringDefault: String = STRING_DEFAULT,
    bigIntegerDefault: BigInteger = BIG_INTEGER_DEFAULT,
    bigDecimalDefault: BigDecimal = BIG_DECIMAL_DEFAULT,
    localTimeDefault: LocalTime = LOCAL_TIME_DEFAULT,
    localDateDefault: LocalDate = LOCAL_DATE_DEFAULT,
    localDateTimeDefault: LocalDateTime = LOCAL_DATE_TIME_DEFAULT,
    durationDefault: Duration = DURATION_DEFAULT,
    datePeriodDefault: DatePeriod = DATE_PERIOD_DEFAULT,
    dateTimePeriodDefault: DateTimePeriod = DATE_TIME_PERIOD_DEFAULT,
    uuidDefault: () -> Uuid = { UUID_DEFAULT }
): Any =
    when (this) {
        Boolean::class -> booleanDefault

        UByte::class -> uByteDefault

        UShort::class -> uShortDefault

        UInt::class -> uIntDefault

        ULong::class -> uLongDefault

        Byte::class -> byteDefault

        Short::class -> shortDefault

        Int::class -> intDefault

        Long::class -> longDefault

        Float::class -> floatDefault

        Double::class -> doubleDefault

        Char::class -> charDefault

        String::class -> stringDefault

        BigInteger::class -> bigIntegerDefault

        BigDecimal::class -> bigDecimalDefault

        LocalTime::class -> localTimeDefault

        LocalDate::class -> localDateDefault

        LocalDateTime::class -> localDateTimeDefault

        Duration::class -> durationDefault

        DatePeriod::class -> datePeriodDefault

        DateTimePeriod::class -> dateTimePeriodDefault

        Uuid::class -> uuidDefault()

        else -> throw IllegalArgumentException("Unknown type \"$simpleName\"")
    }

public fun KClass<*>.parsePrimitiveOrNull(
    value: String,
    dateTimeFormat: DateTimeFormat<DateTimeComponents>? = null,
): Any? =
    when (this) {
        Boolean::class -> value.toBooleanStrictOrNull()

        UByte::class -> value.toUByteOrNull()

        UShort::class -> value.toUShortOrNull()

        UInt::class -> value.toUIntOrNull()

        ULong::class -> value.toULongOrNull()

        Byte::class -> value.toByteOrNull()

        Short::class -> value.toShortOrNull()

        Int::class -> value.toIntOrNull()

        Long::class -> value.toLongOrNull()

        Float::class -> value.toFloatOrNull()

        Double::class -> value.toDoubleOrNull()

        Char::class -> value[0]

        String::class -> value

        BigInteger::class -> BigInteger.parseOrNull(value)

        BigDecimal::class -> BigDecimal.parseOrNull(value)

        LocalTime::class -> dateTimeFormat?.parseOrNull(value)?.toLocalTime()
            ?: LocalTime.parseOrNull(value)

        LocalDate::class -> dateTimeFormat?.parseOrNull(value)?.toLocalDate()
            ?: LocalDate.parseOrNull(value)

        LocalDateTime::class -> dateTimeFormat?.parseOrNull(value)?.toLocalDateTime()
            ?: LocalDateTime.parseOrNull(value)

        Duration::class -> Duration.parseOrNull(value)

        DatePeriod::class -> DatePeriod.parseOrNull(value)

        DateTimePeriod::class -> DateTimePeriod.parseOrNull(value)

        Uuid::class -> Uuid.parseOrNull(value)

        else -> null
    }

public fun KClass<*>.parsePrimitive(
    value: String,
    dateTimeFormat: DateTimeFormat<DateTimeComponents>? = null,
): Any = parsePrimitiveOrNull(value, dateTimeFormat)
    ?: throw IllegalArgumentException("Unknown type '$simpleName'")

@Suppress("UNCHECKED_CAST")
public val KClass<*>.typeArguments: List<KClass<*>>
    get() = (java.genericSuperclass as ParameterizedType)
        .actualTypeArguments.map { type -> (type as Class<*>).kotlin }

////////////////////////////////////////////////////////PROPERTIES//////////////////////////////////////////////////////
public fun KClass<*>.declaredMemberProperty(propertyName: String): KProperty<*>? =
    declaredMemberProperties[propertyName]

public fun KClass<*>.declaredMemberExtensionProperty(propertyName: String): KProperty<*>? =
    declaredMemberExtensionProperties[propertyName]

public fun KClass<*>.memberProperty(propertyName: String): KProperty<*>? = memberProperties[propertyName]

public operator fun KClass<*>.get(propertyName: String): KProperty<*>? = memberProperty(propertyName)

public fun KClass<*>.staticProperty(propertyName: String): KProperty<*>? = staticProperties[propertyName]

public fun KClass<*>.getStaticPropertyOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = staticProperties[propertyName]?.takeIf(predicate)?.invoke()

public fun KClass<*>.getStaticProperty(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = staticProperties[propertyName]?.takeIf(predicate)!!()

/////////////////////////////////////////////////////////FUNCTIONS//////////////////////////////////////////////////////
public fun KClass<*>.declaredMemberFunctions(funName: String): List<KFunction<*>> = declaredMemberFunctions.get(funName)

public fun KClass<*>.declaredMemberFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredMemberFunctions.get(funName, *arguments)

public fun KClass<*>.declaredMemberExtensionFunctions(funName: String): List<KFunction<*>> =
    declaredMemberExtensionFunctions.get(funName)

public fun KClass<*>.declaredMemberExtensionFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredMemberExtensionFunctions.get(funName, *arguments)

public fun KClass<*>.declaredFunctions(funName: String): List<KFunction<*>> =
    declaredFunctions.get(funName)

public fun KClass<*>.declaredFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredFunctions.get(funName, *arguments)

public fun KClass<*>.memberFunctions(funName: String): List<KFunction<*>> =
    memberFunctions.get(funName)

public fun KClass<*>.memberFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    memberFunctions.get(funName, *arguments)

public fun KClass<*>.staticFunctions(funName: String): List<KFunction<*>> = staticFunctions.get(funName)

public fun KClass<*>.staticFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    staticFunctions.get(funName, *arguments)

public fun KClass<*>.callStaticFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    callCallableOrNull(null, funName, *arguments, callable = KClass<*>::staticFunction)

public fun KClass<*>.callStaticFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    callCallable(null, funName, *arguments, callable = KClass<*>::staticFunction)

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun KClass<*>.declaredMembers(memberName: String): List<KCallable<*>> = declaredMembers.get(memberName)

public fun KClass<*>.declaredMember(memberName: String, vararg arguments: KType): KCallable<*>? =
    declaredMembers.get(memberName, *arguments)

public fun KClass<*>.member(memberName: String): List<KCallable<*>> = members.get(memberName)

public fun KClass<*>.member(memberName: String, vararg arguments: KType): KCallable<*>? =
    members.get(memberName, *arguments)

private operator fun Collection<KProperty<*>>.get(propertyName: String): KProperty<*>? =
    find { it.name == propertyName }

private fun <T : KCallable<*>> Collection<T>.get(name: String): List<T> =
    filter { kCallable -> kCallable.name == name }

private fun <T : KCallable<*>> Collection<T>.get(name: String, vararg arguments: KType): T? =
    find { kCallable -> kCallable.hasSignature(name, *arguments) }

public fun KCallable<*>.hasSignature(name: String, vararg arguments: KType): Boolean {
    if (this.name != name) return false

    // Drop instance parameter if it is instance method, not static.
    val parameters = if (instanceParameter == null) parameters else parameters.drop()

    return parameters.size == arguments.size &&
        parameters.map(KParameter::type).zip(arguments).all { (parameterType, argType) ->
            if (parameterType is KTypeParameter)
                (parameterType.classifier as KTypeParameter).upperBounds.any { bound ->
                    argType.classifier == parameterType.classifier || argType.isSubtypeOf(bound)
                }
            else argType.classifier == parameterType.classifier || argType.isSubtypeOf(parameterType)
        }
}

internal fun KClass<*>.callCallableOrNull(
    instance: Any?,
    name: String,
    vararg arguments: Pair<KType, Any?>,
    callable: KClass<*>.(funName: String, arguments: Array<KType>) -> KCallable<*>?
) = callable(
    name,
    arguments.map(Pair<KType, *>::first).toTypedArray(),
)?.invoke(instance, *arguments.map(Pair<*, *>::second).toTypedArray())

internal fun KClass<*>.callCallable(
    instance: Any?,
    name: String,
    vararg arguments: Pair<KType, Any?>,
    callable: KClass<*>.(funName: String, arguments: Array<KType>) -> KCallable<*>?
) = callable(
    name,
    arguments.map(Pair<KType, *>::first).toTypedArray(),
)!!(instance, *arguments.map(Pair<*, *>::second).toTypedArray())

public fun KClass<*>.packageExtensions(packages: Set<String>): Sequence<Method> = sequence {
    val reflections = Reflections(
        ConfigurationBuilder().forPackages(
            *packages.toTypedArray(),
        ).addScanners(
            Scanners.SubTypes,
            Scanners.TypesAnnotated,
            Scanners.MethodsSignature,
        ),
    )

    (reflections.getSubTypesOf(Any::class.java) +
        reflections.getTypesAnnotatedWith(Metadata::class.java))
        .forEach { kClass ->
            try {
                kClass.methods.filter { method ->
                    !method.isSynthetic &&
                        method.parameterTypes.firstOrNull()?.isAssignableFrom(this@packageExtensions.java) == true
                }.forEach { method ->
                    yield(method)
                }
            } catch (e: NoClassDefFoundError) {
            }
        }
}

public fun KClass<*>.packageExtensions(name: String, packages: Set<String>): Sequence<Method> =
    packageExtensions(packages).filter { method -> method.name == name }

public fun Method.hasSignature(name: String, vararg arguments: Class<*>): Boolean {
    if (this.name != name) return false

    // Drop instance parameter if it is instance method, not static.
    val parameters = if (Modifier.isStatic(modifiers)) parameterTypes.toList() else parameterTypes.drop()

    return parameters.size == arguments.size && parameters.zip(arguments).all { (parameterType, argType) ->
        parameterType.isAssignableFrom(argType)
    }
}
