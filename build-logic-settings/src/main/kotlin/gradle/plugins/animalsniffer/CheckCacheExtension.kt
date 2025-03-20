package gradle.plugins.animalsniffer

import kotlinx.serialization.Serializable
import ru.vyarus.gradle.plugin.animalsniffer.CheckCacheExtension

/**
 * Animalsniffer check task must load and parse entire classpath in order to  perform check (otherwise it cant know
 * if class/method is missed from signature or a part of 3rd party library). For projects with a large classpath
 * it could be an issue. Cache intended to solve this problem by building special signature from configured signature
 * and project classpath. First check run will be slower (time to build signature file), but subsequent runs will
 * be very fast.
 *
 *
 * Disabled by default due to known issues, when project specific signature could not be build (signature and jars
 * conflict).
 *
 * @author Vyacheslav Rusakov
 * @since 13.07.2017
 */
@Serializable
internal data class CheckCacheExtension(
    /**
     * Enables check task caching to optimize subsequent check task calls. Useful when check called often
     * without clean. Could be extremely helpful on large classpath (like gradle plugin project).
     *
     *
     * When enabled, extra task used for each animalsniffer (check) task to build source set specific signature:
     * it will merge all configured signatures (from signature configuration) and all jars from classpath.
     *
     *
     * In some cases, cache tasks may fail due to merge conflicts. For example, jdk 8 signature could not be merged
     * with gradle jar because of different xml apis. But in most cases, cache would work.
     */
    val enabled: Boolean? = null,

    /**
     * Option ignored until [.enabled] is enabled . Specifies exclusions for
     * source set specific signature (cache task, used before each check task). Exclusions make generated signature
     * smaller and, as a result, faster check executions.
     *
     *
     * PAY ATTENTION: by default, 'sun.*' and repackaged dependencies in gradle are excluded (the last one makes
     * check much much faster on gradle plugin projects).
     *
     * @see ru.vyarus.gradle.plugin.animalsniffer.info.SignatureInfoTask to look for packages to exclude
     *
     * @see ru.vyarus.gradle.plugin.animalsniffer.signature.BuildSignatureTask.exclude
     */
    val exclude: Set<String?>? = null,
    val setExclude: Set<String?>? = null,
    /**
     * Build separate cache signature for each provided signatures or merge everything into the single signature.
     * First (be default) option useful when provided signatures must be used separately. E.g. when java and android
     * signatures used then 2 cache signatures will be generated (each contain entire classpath). Check task will
     * use both for separate checks.
     *
     *
     * Second case could be useful when signatures must be merged (e.g. small signatures from 3rd party libraries).
     */
    val mergeSignatures: Boolean? = null,
) {

    fun applyTo(recipient: CheckCacheExtension) {
        enabled?.let(recipient::setEnabled)
        exclude?.toTypedArray()?.let(recipient::exclude)
        setExclude?.let(recipient::setExclude)
        mergeSignatures?.let(recipient::setMergeSignatures)
    }
}
