package gradle

import java.util.*

internal infix fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> {
    val resultMap = toMutableMap()
    for (key in source.keys) {
        //recursive merge for nested maps
        if (source[key] is Map<*, *> && resultMap[key] is Map<*, *>) {
            val originalChild = resultMap[key] as Map<String, Any?>
            val newChild = source[key] as Map<String, Any?>
            resultMap[key] = originalChild deepMerge newChild
            //merge for collections
        }
        else if (source[key] is Collection<*> && resultMap[key] is Collection<*>) {
            if (!(resultMap[key] as Collection<*>).containsAll(source[key] as Collection<*>)) {
                resultMap[key] = (resultMap[key] as Collection<*>) + (source[key] as Collection<*>)
            }
        }
        else {
            if (source[key] == null || (source[key] is String && (source[key] as String).isBlank())) continue
            resultMap[key] = source[key] as Any
        }
    }
    return resultMap
}
