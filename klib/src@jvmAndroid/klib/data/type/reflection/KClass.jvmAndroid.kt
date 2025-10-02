package klib.data.type.reflection

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import klib.data.type.collections.drop
import klib.data.type.collections.list.drop
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.KVisibility
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
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder

//////////////////////////////////////////////////////////GENERIC///////////////////////////////////////////////////////
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
        ConfigurationBuilder().forPackages(*packages.toTypedArray())
            .filterInputsBy(
                FilterBuilder().apply {
                    packages.forEach(::includePackage)
                },
            )
            .addScanners(Scanners.SubTypes, Scanners.TypesAnnotated, Scanners.MethodsSignature),
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
            }
            catch (_: NoClassDefFoundError) {
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
