package klib.data.database.mdb.complex

import com.healthmarketscience.jackcess.impl.complex.VersionHistoryColumnInfoImpl
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime

public actual class Version(public val version: com.healthmarketscience.jackcess.complex.Version) :
    ComplexValue by JavaComplexValue(version), Comparable<Version> {
    public actual constructor(value: String, modifiedDate: Any, complexValueFk: ComplexValueForeignKey) :
            this(
                VersionHistoryColumnInfoImpl.newVersion(
                    complexValueFk.complexValueFk,
                    value,
                    modifiedDate
                )
            )

    public actual val value: String
        get() = version.value

    public actual val modifiedLocalDate: LocalDateTime
        get() = version.modifiedLocalDate.toKotlinLocalDateTime()

    public actual val modifiedDateObject: Any
        get() = version.modifiedDateObject

    actual override fun compareTo(other: Version): Int = version.compareTo(other.version)
}
