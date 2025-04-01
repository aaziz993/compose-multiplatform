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
public fun KClass<*>.declaredMemberFunction(funName: String, vararg argKTypes: KType): KFunction<*>? =
    declaredMemberFunctions.get(funName, *argKTypes)

public fun KClass<*>.declaredMemberExtensionFunction(funName: String, vararg argKTypes: KType): KFunction<*>? =
    declaredMemberExtensionFunctions.get(funName, *argKTypes)

public fun KClass<*>.memberFunction(funName: String, vararg argKTypes: KType): KFunction<*>? =
    memberFunctions.get(funName, * argKTypes)

public fun KClass<*>.staticFunction(funName: String, vararg argKTypes: KType): KFunction<*>? =
    staticFunctions.get(funName, *argKTypes)

public fun KClass<*>.callStaticFunction(funName: String, vararg argKTypes: Pair<Any?, KType>): Any? =
    staticFunction(
        funName,
        *argKTypes.map(Pair<*, KType>::second)
            .toTypedArray(),
    )!!(*argKTypes.map(Pair<*, *>::first).toTypedArray())

public fun KClass<*>.declaredFunction(funName: String, vararg argKTypes: KType): KFunction<*>? =
    declaredFunctions.get(funName, *argKTypes)

//////////////////////////////////////////////////////////MEMBERS///////////////////////////////////////////////////////
public fun KClass<*>.declaredMember(memberName: String, vararg argKTypes: KType): KCallable<*>? = declaredMembers.get(memberName, * argKTypes)

public fun KClass<*>.member(memberName: String, vararg argKTypes: KType): KCallable<*>? = members.get(memberName, * argKTypes)

private operator fun Collection<KProperty<*>>.get(propertyName: String): KProperty<*>? = find { it.name == propertyName }

private fun <T : KCallable<*>> Collection<T>.get(funName: String, vararg argKTypes: KType): T? {
    val parametersSize = argKTypes.size + 1

    return find { function ->
        function.name == funName && function.parameters.size == parametersSize
        function.parameters.drop(1).map(KParameter::type).zip(argKTypes).all { (parameterType, argKType) ->
            if (parameterType.classifier is KTypeParameter) {
                (parameterType.classifier as KTypeParameter).upperBounds.contains(argKType)
            }
            else {
                parameterType == argKType
            }
        }
    }
}
