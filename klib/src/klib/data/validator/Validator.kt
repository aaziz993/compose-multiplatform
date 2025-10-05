package klib.data.validator

import kotlinx.serialization.Serializable

@Serializable
public data class Validator(
    public val type: Validation = Validation.FAIL_FAST,
    public val rules: List<ValidatorRule>,
    public val required: Boolean = true,
) {

    public fun validate(input: String): List<String> =
        if (input.isEmpty() && !required) {
            emptyList()
        }
        else when (type) {
            Validation.FAIL_FAST -> rules.firstNotNullOfOrNull { it.validate(input) }?.let { listOf(it) }
                .orEmpty()

            Validation.LAZY_EVAL -> rules.mapNotNull { it.validate(input) }
        }

    public companion object {

        public fun nonEmpty(emptyMessage: String = "Value is empty"): Validator = Validator(
            rules = listOf(
                ValidatorRule.nonEmpty(emptyMessage),
            ),
        )

        public fun numericPhone(
            required: Boolean = true,
            emptyMessage: String = "Value is empty",
            lengthMessage: String = "Value length is invalid",
            prefixMessage: String = "Value prefix is not plus",
            digitsMessage: String = "Value has not digits",
            lettersMessage: String = "Value has letters",
            patternMessage: String = "Value is invalid",
        ): Validator = Validator(
            rules = listOf(
                ValidatorRule.nonEmpty(emptyMessage),
                ValidatorRule.numericPhoneLength(lengthMessage),
                ValidatorRule.startsWith("+", message = prefixMessage),
                ValidatorRule.containsDigit(digitsMessage),
                ValidatorRule.nonLetters(lettersMessage),
                ValidatorRule.numericPhonePattern(patternMessage),
            ),
            required = required,
        )

        public fun delimitedPhone(
            required: Boolean = true,
            emptyMessage: String = "Value is empty",
            lengthMessage: String = "Value length is invalid",
            prefixMessage: String = "Value prefix is not plus",
            digitsMessage: String = "Value has not digits",
            lettersMessage: String = "Value has letters",
            patternMessage: String = "Value is invalid",
        ): Validator = Validator(
            rules = listOf(
                ValidatorRule.nonEmpty(emptyMessage),
                ValidatorRule.delimitedPhoneLength(lengthMessage),
                ValidatorRule.startsWith("+", message = prefixMessage),
                ValidatorRule.containsDigit(digitsMessage),
                ValidatorRule.nonLetters(lettersMessage),
                ValidatorRule.delimitedPhonePattern(patternMessage),
            ),
            required = required,
        )

        public fun email(
            required: Boolean = true,
            emptyMessage: String = "Value is empty",
            lengthMessage: String = "Value length is invalid",
            whitespaceMessage: String = "Value has whitespace",
            patternMessage: String = "Value is invalid",
        ): Validator = Validator(
            rules = listOf(
                ValidatorRule.nonEmpty(emptyMessage),
                ValidatorRule.emailLength(lengthMessage),
                ValidatorRule.nonWhitespace(whitespaceMessage),
                ValidatorRule.emailPattern(patternMessage),
            ),
            required = required,
        )

        public fun kotlinDuration(
            required: Boolean = true,
            patternMessage: String = "Value is invalid duration",
        ): Validator = Validator(
            rules = listOf(
                ValidatorRule.kotlinDurationPattern(patternMessage),
            ),
            required = required,
        )
    }
}
