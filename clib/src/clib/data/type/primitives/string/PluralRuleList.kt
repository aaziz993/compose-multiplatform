@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.data.type.primitives.string

import org.jetbrains.compose.resources.AsyncCache
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.LanguageQualifier
import org.jetbrains.compose.resources.RegionQualifier
import org.jetbrains.compose.resources.plural.PluralRule
import org.jetbrains.compose.resources.plural.PluralCategory
import org.jetbrains.compose.resources.plural.cldrPluralRuleListIndexByLocale
import org.jetbrains.compose.resources.plural.cldrPluralRuleLists

internal class PluralRuleList(val rules: Array<PluralRule>) {

    fun getCategory(quantity: Int): PluralCategory =
        rules.firstOrNull { rule -> rule.appliesTo(quantity) }?.category ?: PluralCategory.OTHER

    fun getCategoryIndex(quantity: Int): Int = rules.indexOfFirst { rule -> rule.appliesTo(quantity) }

    companion object {

        private val cache = AsyncCache<Int, PluralRuleList>()
        private val emptyList = PluralRuleList(emptyArray<PluralRule>())

        @OptIn(InternalResourceApi::class)
        suspend fun getInstance(
            languageQualifier: LanguageQualifier,
            regionQualifier: RegionQualifier,
        ): PluralRuleList {
            val cldrLocaleName = buildCldrLocaleName(languageQualifier, regionQualifier) ?: return emptyList
            return getInstance(cldrLocaleName)
        }

        suspend fun getInstance(cldrLocaleName: String): PluralRuleList {
            val listIndex = cldrPluralRuleListIndexByLocale[cldrLocaleName]!!
            return cache.getOrLoad(listIndex) { createInstance(listIndex) }
        }

        @OptIn(InternalResourceApi::class)
        private fun buildCldrLocaleName(
            languageQualifier: LanguageQualifier,
            regionQualifier: RegionQualifier,
        ): String? {
            val localeWithRegion = languageQualifier.language + "_" + regionQualifier.region
            if (cldrPluralRuleListIndexByLocale.containsKey(localeWithRegion)) {
                return localeWithRegion
            }
            if (cldrPluralRuleListIndexByLocale.containsKey(languageQualifier.language)) {
                return languageQualifier.language
            }
            return null
        }

        private fun createInstance(cldrPluralRuleListIndex: Int): PluralRuleList {
            val cldrPluralRuleList = cldrPluralRuleLists[cldrPluralRuleListIndex]
            val pluralRules = cldrPluralRuleList.map { PluralRule(it.first, it.second) }
            return PluralRuleList(pluralRules.toTypedArray<PluralRule>())
        }
    }
}
