package klib.data.type.primitives.time

import com.ucasoft.kcron.Cron
import com.ucasoft.kcron.core.builders.Builder
import com.ucasoft.kcron.core.settings.Settings
import com.ucasoft.kcron.kotlinx.datetime.CronLocalDateTime
import com.ucasoft.kcron.kotlinx.datetime.CronLocalDateTimeProvider
import kotlinx.datetime.LocalDateTime

public fun String.toCronBuilder(
    block: (Settings) -> Unit = {}
): Builder<LocalDateTime, CronLocalDateTime, CronLocalDateTimeProvider> = Cron.parseAndBuild(this, block)
