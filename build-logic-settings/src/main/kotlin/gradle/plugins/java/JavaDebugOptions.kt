package gradle.plugins.java

import gradle.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.process.JavaDebugOptions

/**
 * Contains a subset of the [Java Debug Wire Protocol](https://docs.oracle.com/javase/8/docs/technotes/guides/jpda/conninv.html) properties.
 *
 * @since 5.6
 */
@Serializable
internal data class JavaDebugOptions(
    /**
     * Should the debug agent start in the forked process? By default, this is false.
     */
    val enabled: Boolean? = null,

    /**
     * Host address to listen on or connect to when debug is enabled. By default, no host is set.
     *
     *
     *
     * When run in [server][.getServer] mode, the process listens on the loopback address on Java 9+ and all interfaces on Java 8 and below by default.
     * Setting host to `*` will make the process listen on all network interfaces. This is not supported on Java 8 and below.
     * Setting host to anything else will make the process listen on that address.
     *
     *
     *
     * When run in [client][.getServer] mode, the process attempts to connect to the given host and [.getPort].
     *
     *
     * @since 7.6
     */
    val host: String? = null,

    /**
     * The debug port to listen on or connect to.
     */
    val port: Int? = null,

    /**
     * Should the process listen for a debugger to attach (server) or immediately connect to an already running debugger (client)?
     *
     *
     * In server mode (`server = true`), the process listens for a debugger to connect after the JVM starts up.
     *
     *
     *
     * In client mode (`server = false`), the process attempts to connect to an already running debugger.
     *
     */
    val server: Boolean? = null,

    /**
     * Should the process suspend until the connection to the debugger is established?
     */
    val suspend: Boolean? = null,
) {

    @Suppress("UnstableApiUsage")
    fun applyTo(options: JavaDebugOptions) {
        options.enabled tryAssign enabled
        options.host tryAssign host
        options.port tryAssign port
        options.server tryAssign server
        options.suspend tryAssign suspend
    }
}
