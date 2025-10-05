package clib.presentation

public fun noLocalProvidedFor(name: String): Nothing = error("CompositionLocal $name not present")
