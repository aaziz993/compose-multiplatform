package plugin.project.gradle.spotless.model

import com.diffplug.gradle.spotless.RomeStepConfig

internal interface RomeStepConfig {

    /**
     * Optional path to the directory with configuration file for Biome. The file
     * must be named `biome.json`. When none is given, the default
     * configuration is used. If this is a relative path, it is resolved against the
     * project's base directory.
     */
    val configPath: String?

    /**
     * Optional directory where the downloaded Biome executable is placed. If this
     * is a relative path, it is resolved against the project's base directory.
     * Defaults to
     * `~/.m2/repository/com/diffplug/spotless/spotless-data/biome`.
     */

    val downloadDir: String?

    /**
     * Optional path to the Biome executable. Either a `version` or a
     * `pathToExe` should be specified. When not given, an attempt is
     * made to download the executable for the given version from the network. When
     * given, the executable is used and the `version` parameter is
     * ignored.
     *
     *
     * When an absolute path is given, that path is used as-is. When a relative path
     * is given, it is resolved against the project's base directory. When only a
     * file name (i.e. without any slashes or back slash path separators such as
     * `biome`) is given, this is interpreted as the name of a command
     * with executable that is in your `path` environment variable. Use
     * `./executable-name` if you want to use an executable in the
     * project's base directory.
     */
    val pathToExe: String?

    /**
     * Biome version to download, applies only when no <code>pathToExe</code> is
     * specified explicitly. Either a <code>version</code> or a
     * <code>pathToExe</code> should be specified. When not given, a default known
     * version is used. For stable builds, it is recommended that you always set the
     * version explicitly. This parameter is ignored when you specify a
     * <code>pathToExe</code> explicitly.
     */
    val version: String?

    fun applyTo(config: RomeStepConfig<*>) {
        configPath?.let(config::configPath)
        downloadDir?.let(config::downloadDir)
        pathToExe?.let(config::pathToExe)
    }
}
