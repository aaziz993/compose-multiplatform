package klib

public actual fun getPlatform(): Platform = JvmPlatform("Java ${System.getProperty("java.version")}")
