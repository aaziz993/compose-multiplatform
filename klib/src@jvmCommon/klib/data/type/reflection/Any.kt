package klib.data.type.reflection

import klib.data.type.collections.deepGet
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KVisibility

////////////////////////////////////////////////////////PROPERTIES//////////////////////////////////////////////////////
public fun Any.getDeclaredMemberPropertyOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.declaredMemberProperty(propertyName)?.takeIf(predicate)?.invoke(this)

public fun Any.getDeclaredMemberProperty(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.declaredMemberProperty(propertyName)?.takeIf(predicate)!!(this)

public fun Any.getDeclaredMemberExtensionPropertyOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.declaredMemberProperty(propertyName)?.takeIf(predicate)?.invoke(this)

public fun Any.getDeclaredMemberExtensionProperty(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.declaredMemberProperty(propertyName)?.takeIf(predicate)!!(this)

public fun Any.getMemberPropertyOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class[propertyName]?.takeIf(predicate)?.invoke(this)

public fun Any.getMemberProperty(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class[propertyName]?.takeIf(predicate)!!(this)

public fun Any.getOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = getMemberPropertyOrNull(propertyName, predicate)

public fun Any.deepGetOrNull(
    vararg propertyNamePath: String,
    predicate: List<Pair<Any, Any?>>.(KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Pair<List<Pair<Any, Any?>>, Any?> = deepGet(*propertyNamePath) {
    last().first.getOrNull(last().second) { property -> predicate(property) }
}

public fun Any.deepGet(
    vararg propertyNamePath: String,
    predicate: List<Pair<Any, Any?>>.(KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Pair<List<Pair<Any, Any?>>, Any?> = deepGet(*propertyNamePath) {
    last().first.getMemberProperty(last().second) { property -> predicate(property) }
}

public operator fun Any.get(propertyName: String): Any? = getMemberProperty(propertyName)

public fun Any.getStaticPropertyOrNull(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.getStaticPropertyOrNull(propertyName, predicate)

public fun Any.getStaticProperty(
    propertyName: String,
    predicate: (KProperty<*>) -> Boolean = { property ->
        property.visibility == KVisibility.PUBLIC
    }
): Any? = this::class.getStaticProperty(propertyName, predicate)

/////////////////////////////////////////////////////////FUNCTIONS//////////////////////////////////////////////////////
public fun Any.callDeclaredMemberFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, funName, *arguments, callable = KClass<*>::declaredMemberFunction)

public fun Any.callDeclaredMemberFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, funName, *arguments, callable = KClass<*>::declaredMemberFunction)

public fun Any.callDeclaredMemberExtensionFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, funName, *arguments, callable = KClass<*>::declaredMemberExtensionFunction)

public fun Any.callDeclaredMemberExtensionFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, funName, *arguments, callable = KClass<*>::declaredMemberExtensionFunction)

public fun Any.callDeclaredFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, funName, *arguments, callable = KClass<*>::declaredFunction)

public fun Any.callDeclaredFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, funName, *arguments, callable = KClass<*>::declaredFunction)

public fun Any.callMemberFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, funName, *arguments, callable = KClass<*>::memberFunction)

public fun Any.callMemberFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, funName, *arguments, callable = KClass<*>::memberFunction)

public fun Any.invokeOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callMemberFunctionOrNull(funName, *arguments)

public operator fun Any.invoke(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callMemberFunction(funName, *arguments)

public fun Any.callStaticFunctionOrNull(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callStaticFunctionOrNull(funName, *arguments)

public fun Any.callStaticFunction(funName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callStaticFunction(funName, *arguments)

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun Any.callDeclaredMemberOrNull(memberName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, memberName, *arguments, callable = KClass<*>::declaredMember)

public fun Any.callDeclaredMember(memberName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, memberName, *arguments, callable = KClass<*>::declaredMember)

public fun Any.callMemberOrNull(memberName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallableOrNull(this, memberName, *arguments, callable = KClass<*>::member)

public fun Any.callMember(memberName: String, vararg arguments: Pair<KType, Any?>): Any? =
    this::class.callCallable(this, memberName, *arguments, callable = KClass<*>::member)
