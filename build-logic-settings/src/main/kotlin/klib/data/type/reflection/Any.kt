package klib.data.type.reflection

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KType

@Suppress("UNCHECKED_CAST")
public fun Any.genericTypes(): Array<Class<*>> =
    (javaClass.getGenericSuperclass() as ParameterizedType)
        .actualTypeArguments as Array<Class<*>>

////////////////////////////////////////////////////////PROPERTIES//////////////////////////////////////////////////////
public fun Any.declaredMemberPropertyValue(propertyName: String): Any? = this::class.declaredMemberProperty(propertyName)!!(this)

public fun Any.declaredMemberExtensionPropertyValue(propertyName: String): Any? = this::class.declaredMemberProperty(propertyName)!!(this)

public fun Any.memberPropertyValue(propertyName: String): Any? = this::class[propertyName]!!(this)

public operator fun Any.get(propertyName: String): Any? = memberPropertyValue(propertyName)

public fun Any.staticPropertyValue(propertyName: String): Any? = this::class.declaredMemberProperty(propertyName)!!()

/////////////////////////////////////////////////////////FUNCTIONS//////////////////////////////////////////////////////
public fun Any.callDeclaredMemberFunction(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.declaredMemberFunction(
        funName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())

public fun Any.callDeclaredMemberExtensionFunction(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.declaredMemberExtensionFunction(
        funName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())

public fun Any.callMemberFunction(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.memberFunction(
        funName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())

public operator fun Any.invoke(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? = callMemberFunction(funName, *argKTypes)

public fun Any.callDeclaredFunction(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.declaredFunction(
        funName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun Any.callDeclaredMember(memberName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.declaredMember(
        memberName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())

public fun Any.callMember(memberName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    this::class.member(
        memberName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *argKTypes.map(Pair<*, *>::first).toTypedArray())
