package clib.presentation.components.picker.country

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import klib.data.location.locale.Locale
import klib.data.location.locale.current
import klib.data.validator.ValidatorRule

/**
 * Multiplatform phone transformer simulating Android PhoneNumberUtil.AsYouTypeFormatter
 * Groups digits according to simple country rules (US-style default)
 */
public class CountryPickerTransformer(
    countryIso: String = Locale.current.country()!!.toString()
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val rawDigits = text.text.filter { it.isDigit() || it == '+' }

        // Example US-style grouping: +X (XXX) XXX-XXXX
        val formatted = buildString {
            var index = 0
            rawDigits.forEach { c ->
                when {
                    index == 0 && c == '+' -> append(c) // keep plus
                    index == 1 -> append('(').append(c)
                    index in 2..4 -> append(c)
                    index == 5 -> append(c).append(") ")
                    index in 6..8 -> append(c)
                    index == 9 -> append(c).append('-')
                    else -> append(c)
                }
                index++
            }
        }

        // Compute offset mapping
        val originalToTransformed = IntArray(rawDigits.length) { 0 }
        val transformedToOriginal = IntArray(formatted.length) { 0 }

        var origIndex = 0
        for (i in formatted.indices) {
            if (formatted[i].isDigit() || formatted[i] == '+') {
                transformedToOriginal[i] = origIndex
                originalToTransformed[origIndex] = i
                origIndex++
            }
            else {
                transformedToOriginal[i] = origIndex.coerceAtMost(rawDigits.length)
            }
        }

        return TransformedText(
            AnnotatedString(formatted),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    offset.coerceIn(originalToTransformed.indices).let { originalToTransformed[it] }

                override fun transformedToOriginal(offset: Int): Int =
                    offset.coerceIn(transformedToOriginal.indices).let { transformedToOriginal[it] }
            },
        )
    }
}
