package klib.data.type.reflection

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KType
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.typeOf

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
public fun Any.callDeclaredMemberFunction(funName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.declaredMemberFunction(
        funName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *arguments.map(Pair<*, *>::first).toTypedArray())

public fun Any.callDeclaredMemberExtensionFunction(funName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.declaredMemberExtensionFunction(
        funName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *arguments.map(Pair<*, *>::first).toTypedArray())

public fun Any.callMemberFunction(funName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.memberFunction(
        funName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *arguments.map(Pair<*, *>::first).toTypedArray())

public operator fun Any.invoke(funName: String, vararg arguments: Pair<Any?, KType>): Any? = callMemberFunction(funName, *arguments)

public fun Any.callDeclaredFunction(funName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.declaredFunction(
        funName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(this, *arguments.map(Pair<*, *>::first).toTypedArray())

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun Any.callDeclaredMember(memberName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.declaredMember(
        memberName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!.let { declaredMember ->
        if (declaredMember.instanceParameter == null)
            declaredMember(* arguments.map(Pair<*, *>::first).toTypedArray())
        else declaredMember(this, * arguments.map(Pair<*, *>::first).toTypedArray())
    }

public fun Any.callDeclaredMember(memberName: String, arg: String): Any? =
    callDeclaredMember(memberName, *arrayOf(arg to typeOf<String>()))

public fun Any.declaredMemberGetter(key: String): (String) -> Any? = { callMember(key, it) }

public fun Any.callMember(memberName: String, vararg arguments: Pair<Any?, KType>): Any? =
    this::class.member(
        memberName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!.let { member ->
        if (member.instanceParameter == null)
            member(*arguments.map(Pair<*, *>::first).toTypedArray())
        else member(this, *arguments.map(Pair<*, *>::first).toTypedArray())
    }

public fun Any.callMember(memberName: String, arg: String): Any? =
    callMember(memberName, *arrayOf(arg to typeOf<String>()))

public fun Any.memberGetter(key: String): (String) -> Any? = { callMember(key, it) }
