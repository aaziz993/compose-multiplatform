package klib

public sealed interface Platform {

    public val name: String
}

public class JvmPlatform(override val name: String) : Platform

public class AndroidPlatform(override val name: String) : Platform

public sealed interface NativePlatform : Platform

public data class AndroidNativePlatform(override val name: String) : NativePlatform

public sealed interface ApplePlatform : NativePlatform
public data class IosPlatform(override val name: String) : ApplePlatform
public data class WatchosPlatform(override val name: String) : ApplePlatform
public data class TVosPlatform(override val name: String) : ApplePlatform
public data class MacosPlatform(override val name: String) : ApplePlatform

public data class LinuxPlatform(override val name: String) : NativePlatform

public data class MingwPlatform(override val name: String) : NativePlatform

public sealed interface JsPlatform : NativePlatform
public data class BrowserPlatform(override val name: String) : JsPlatform
public data class NodePlatform(override val name: String) : JsPlatform

public expect fun getPlatform(): Platform
