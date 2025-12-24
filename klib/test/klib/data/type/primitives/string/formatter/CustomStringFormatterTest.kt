package klib.data.type.primitives.string.formatter

import klib.data.type.primitives.string.formatter.conversions.conversion
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.text.iterator

class CustomStringFormatterTest {

    @Test
    fun testCustomConversions() {
        val form = buildFormatter {
            takeFrom(StringFormatter.Default)
            conversions {
                'g'(
                    conversion
                    { to, _, arg ->
                        var n = 0
                        for (ch in arg.toString()) {
                            to.append(if (n % 2 == 0) ch.uppercaseChar() else ch.lowercaseChar())
                            n++
                        }
                    },
                )
            }
        }
        assertEquals("Test", form.format("%s", "Test"))
        assertEquals("FuZzY StRiNg YaY", form.format("%g %g %g", "FUzzY", "STRING", "yay"))
    }

    @Test
    fun testCustomFlags() {
        val flagCaseSwitch = '!'
        val form = buildFormatter {
            takeFrom(StringFormatter.Default)
            conversions {
                'q'(
                    conversion(supportedFlags = charArrayOf(flagCaseSwitch))
                    { str, arg ->
                        arg.toString().run { if (flagCaseSwitch in str.flags) uppercase() else lowercase() }
                    },
                )
            }
            flags {
                +flagCaseSwitch
            }
        }
        assertEquals("Test", form.format("%s", "Test"))
        assertFailsWith<FlagMismatchException> { form.format("%!s", "Test") }
        assertEquals("TEATIME", form.format("%!q", "teaTIME"))
        assertEquals("teatime", form.format("%q", "TEAtime"))
        assertFailsWith<FlagMismatchException> { form.format("%#q", "Test") }
    }
}
