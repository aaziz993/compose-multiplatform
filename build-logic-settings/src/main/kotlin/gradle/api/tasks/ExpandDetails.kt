package gradle.api.tasks

import gradle.api.tryAssign
import kotlinx.serialization.Serializable

/**
 * Additional configuration parameters for [gradle.api.file.ContentFilterable.expand] action.
 *
 * @since 7.2
 */
internal interface ExpandDetails<T : org.gradle.api.file.ExpandDetails> {

    /**
     * Controls if the underlying [groovy.text.SimpleTemplateEngine] escapes backslashes in the file before processing. If this is set to `false` then escape sequences in the processed
     * files (`\n`, `\t`, `\\`, etc) are converted to the symbols that they represent, so, for example `\n` becomes newline. If set to `true` then escape sequences are
     * left as is.
     *
     *
     * Default value is `false`.
     *
     * @see groovy.text.SimpleTemplateEngine.setEscapeBackslash
     */
    val escapeBackslash: Boolean?

    fun applyTo(receiver: T) {
        receiver.escapeBackslash tryAssign escapeBackslash
    }
}

@Serializable
internal data class ExpandDetailsImpl(
    override val escapeBackslash: Boolean? = null,
) : ExpandDetails<org.gradle.api.file.ExpandDetails>
