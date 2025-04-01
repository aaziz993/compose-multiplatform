package gradle.api.file

import gradle.api.tasks.Expand
import klib.data.type.collection.SerializableAnyMap
import org.gradle.api.Project
import org.gradle.api.file.ContentFilterable

/**
 * Represents some binary resource whose content can be filtered.
 */
internal interface ContentFilterable<T : ContentFilterable> {

    /**
     *
     * Expands property references in each file as it is copied. More specifically, each file is transformed using
     * Groovy's [groovy.text.SimpleTemplateEngine]. This means you can use simple property references, such as
     * `$property` or `${property}` in the file. You can also include arbitrary Groovy code in the
     * file, such as `${version ?: 'unknown'}` or `${classpath*.name.join(' ')}`
     *
     *
     * Note that all escape sequences (`\n`, `\t`, `\\`, etc) are converted to the symbols
     * they represent, so, for example, `\n` becomes newline. If this is undesirable then [.expand]
     * should be used to disable this behavior.
     *
     * @param properties reference-to-value map for substitution
     * @return this
     */
    val expand: SerializableAnyMap?

    /**
     *
     * Expands property references in each file as it is copied. More specifically, each file is transformed using
     * Groovy's [groovy.text.SimpleTemplateEngine]. This means you can use simple property references, such as
     * `$property` or `${property}` in the file. You can also include arbitrary Groovy code in the
     * file, such as `${version ?: 'unknown'}` or `${classpath*.name.join(' ')}`. The template
     * engine can be configured with the provided action.
     *
     *
     * Note that by default all escape sequences (`\n`, `\t`, `\\`, etc) are converted to the symbols
     * they represent, so, for example, `\n` becomes newline. This behavior is controlled by
     * [gradle.api.tasks.ExpandDetails.getEscapeBackslash] property. It should be set to `true` to disable escape sequences
     * conversion:
     * <pre>
     * expand(one: '1', two: 2) {
     * escapeBackslash = true
     * }
    </pre> *
     *
     * @param properties reference-to-value map for substitution
     * @param action action to perform additional configuration of the underlying template engine
     * @return this
     * @since 7.2
     */
    val expandDetails: Expand?

    context(Project)
    fun applyTo(receiver: T) {
        expand?.let(receiver::expand)
        expandDetails?.let { expandDetails ->
            receiver.expand(expandDetails.properties) {
                expandDetails.expandDetails.applyTo(this)
            }
        }
    }
}
