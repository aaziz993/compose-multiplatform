package clib.presentation.components.picker.country

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.validator.ValidatorRule

/**
 * Multiplatform phone input transformer.
 * Does lightweight formatting of digits and "+" prefix.
 * Cursor is assumed to be at the end.
 */
public class CountryPickerTransformer(
    countryIso: String = Locale.current.country()!!.toString()
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text, text.length) // cursor at end

        return TransformedText(
            AnnotatedString(transformation.formatted.orEmpty()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return transformation.originalToTransformed[offset.coerceIn(transformation.originalToTransformed.indices)]
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return transformation.transformedToOriginal[offset.coerceIn(transformation.transformedToOriginal.indices)]
                }
            },
        )
    }

    private fun reformat(s: CharSequence, cursor: Int): Transformation {
        val rawDigits = s.filter { !ValidatorRule.isDelimitedPhoneSeparator(it) }

        // Basic grouping logic for readability (3-digit groups)
        val formatted = buildString {
            rawDigits.forEachIndexed { index, c ->
                append(c)
                if (index > 0 && index % 3 == 2 && index != rawDigits.lastIndex) append(' ')
            }
        }

        // Mapping offsets
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0

        formatted.forEachIndexed { index, char ->
            if (!ValidatorRule.isDelimitedPhoneSeparator(char)) {
                specialCharsCount++
                transformedToOriginal.add(index - specialCharsCount)
            }
            else {
                originalToTransformed.add(index)
                transformedToOriginal.add(index - specialCharsCount)
            }
        }

        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>
    )
}
