package klib.data.type.reflection

import java.lang.reflect.ParameterizedType
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberExtensionProperties
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.full.staticProperties

//////////////////////////////////////////////////////////GENERIC///////////////////////////////////////////////////////
@Suppress("UNCHECKED_CAST")
public fun KClass<*>.genericTypes(): Array<Class<*>> =
    (java.getGenericSuperclass() as ParameterizedType)
        .actualTypeArguments as Array<Class<*>>

////////////////////////////////////////////////////////PROPERTIES//////////////////////////////////////////////////////
public fun KClass<*>.declaredMemberProperty(propertyName: String): KProperty<*>? = declaredMemberProperties[propertyName]

public fun KClass<*>.declaredMemberExtensionProperty(propertyName: String): KProperty<*>? = declaredMemberExtensionProperties[propertyName]

public fun KClass<*>.memberProperty(propertyName: String): KProperty<*>? = memberProperties[propertyName]

public operator fun KClass<*>.get(propertyName: String): KProperty<*>? = memberProperty(propertyName)

public fun KClass<*>.staticProperty(propertyName: String): KProperty<*>? = staticProperties[propertyName]

/////////////////////////////////////////////////////////FUNCTIONS//////////////////////////////////////////////////////
public fun KClass<*>.declaredMemberFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredMemberFunctions.get(funName, *arguments)

public fun KClass<*>.declaredMemberExtensionFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredMemberExtensionFunctions.get(funName, *arguments)

public fun KClass<*>.memberFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    memberFunctions.get(funName, * arguments)

public fun KClass<*>.staticFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    staticFunctions.get(funName, *arguments)

public fun KClass<*>.callStaticFunction(funName: String, vararg arguments: Pair<Any?, KType>): Any? =
    staticFunction(
        funName,
        *arguments.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(*arguments.map(Pair<*, *>::first).toTypedArray())

public fun KClass<*>.declaredFunction(funName: String, vararg arguments: KType): KFunction<*>? =
    declaredFunctions.get(funName, *arguments)

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun KClass<*>.declaredMember(memberName: String, vararg arguments: KType): KCallable<*>? = declaredMembers.get(memberName, * arguments)

public fun KClass<*>.member(memberName: String, vararg arguments: KType): KCallable<*>? = members.get(memberName, * arguments)

private operator fun Collection<KProperty<*>>.get(propertyName: String): KProperty<*>? = find { it.name == propertyName }

private fun <T : KCallable<*>> Collection<T>.get(funName: String, vararg arguments: KType): T? =
    find { kCallable ->
        if (kCallable.name != funName) {
            return@find false
        }
        // Drop instance parameter if it is instance method, not static.
        val parameters = if (kCallable.instanceParameter == null) kCallable.parameters
        else kCallable.parameters.drop(1)

        parameters.size == arguments.size &&
            parameters.map(KParameter::type).zip(arguments).all { (parameterType, argType) ->
                if (parameterType is KTypeParameter)
                    (parameterType.classifier as KTypeParameter).upperBounds.any { bound ->
                        argType.classifier == parameterType.classifier || argType.isSubtypeOf(bound)
                    }
                else argType.classifier == parameterType.classifier || argType.isSubtypeOf(parameterType)
            }
    }
