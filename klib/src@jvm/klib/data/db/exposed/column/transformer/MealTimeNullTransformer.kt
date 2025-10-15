package klib.data.db.exposed.column.transformer

import klib.data.db.exposed.column.transformer.model.Meal
import kotlinx.datetime.LocalTime
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnTransformer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.time

public object MealTimeNullTransformer : ColumnTransformer<LocalTime, Meal?> {

    override fun wrap(value: LocalTime): Meal? = when {
        value.hour == 0 && value.minute == 0 -> null
        value.hour < 10 -> Meal.BREAKFAST
        value.hour < 15 -> Meal.LUNCH
        else -> Meal.DINNER
    }

    override fun unwrap(value: Meal?): LocalTime = when (value) {
        Meal.BREAKFAST -> LocalTime(8, 0)
        Meal.LUNCH -> LocalTime(12, 0)
        Meal.DINNER -> LocalTime(18, 0)
        else -> LocalTime(0, 0)
    }
}

public fun Table.mealTimeNull(column: Column<LocalTime>): Column<Meal?> = column.nullTransform(MealTimeNullTransformer)
