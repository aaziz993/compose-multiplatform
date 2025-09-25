package klib.data.type.serialization.annotations

import kotlinx.serialization.InheritableSerialInfo

@InheritableSerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
public annotation class SerialScript
