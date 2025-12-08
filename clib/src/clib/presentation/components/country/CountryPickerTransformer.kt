package clib.presentation.components.country

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import io.michaelrocks.libphonenumber.kotlin.PhoneNumberUtil
import klib.data.validator.ValidatorRule

public class CountryPickerTransformer(phoneNumberUtil: PhoneNumberUtil, countryCode: String) : VisualTransformation {

    private val phoneNumberFormatter = phoneNumberUtil.getAsYouTypeFormatter(countryCode)

    override fun filter(text: AnnotatedString): TransformedText {
        val transformation = reformat(text, text.length)

        return TransformedText(
            AnnotatedString(transformation.formatted.orEmpty()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int =
                    transformation.originalToTransformed[offset.coerceIn(transformation.originalToTransformed.indices)]

                override fun transformedToOriginal(offset: Int): Int =
                    transformation.transformedToOriginal[offset.coerceIn(transformation.transformedToOriginal.indices)]
            },
        )
    }

    private fun reformat(s: CharSequence, cursor: Int): Transformation {
        phoneNumberFormatter.clear()

        val curIndex = cursor - 1
        var formatted: String? = null
        var lastNonSeparator = 0.toChar()
        var hasCursor = false

        s.forEachIndexed { index, char ->
            if (!ValidatorRule.isDelimitedPhoneSeparator(char)) {
                if (lastNonSeparator.code != 0) {
                    formatted = getFormattedNumber(lastNonSeparator, hasCursor)
                    hasCursor = false
                }
                lastNonSeparator = char
            }
            if (index == curIndex) hasCursor = true
        }

        if (lastNonSeparator.code != 0) formatted = getFormattedNumber(lastNonSeparator, hasCursor)
        val originalToTransformed = mutableListOf<Int>()
        val transformedToOriginal = mutableListOf<Int>()
        var specialCharsCount = 0
        formatted?.forEachIndexed { index, char ->
            transformedToOriginal.add(index - specialCharsCount)
            if (ValidatorRule.isDelimitedPhoneSeparator(char)) specialCharsCount++
            else originalToTransformed.add(index)
        }
        originalToTransformed.add(originalToTransformed.maxOrNull()?.plus(1) ?: 0)
        transformedToOriginal.add(transformedToOriginal.maxOrNull()?.plus(1) ?: 0)

        return Transformation(formatted, originalToTransformed, transformedToOriginal)
    }

    private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String? =
        if (hasCursor) phoneNumberFormatter.inputDigitAndRememberPosition(lastNonSeparator)
        else phoneNumberFormatter.inputDigit(lastNonSeparator)

    private data class Transformation(
        val formatted: String?,
        val originalToTransformed: List<Int>,
        val transformedToOriginal: List<Int>
    )
}
