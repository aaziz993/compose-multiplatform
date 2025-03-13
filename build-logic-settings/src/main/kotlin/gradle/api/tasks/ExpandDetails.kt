package gradle.api.tasks

import gradle.api.tryAssign
import org.gradle.api.file.ExpandDetails

/**
 * Additional configuration parameters for [ContentFilterable.expand] action.
 *
 * @since 7.2
 */
internal interface ExpandDetails {

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

    fun applyTo(details: ExpandDetails) {
        details.escapeBackslash tryAssign escapeBackslash
    }
}
