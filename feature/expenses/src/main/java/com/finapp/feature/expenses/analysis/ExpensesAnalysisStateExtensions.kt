package com.finapp.feature.expenses.analysis

import com.finapp.core.data.api.model.Category
import com.finapp.core.data.api.model.Transaction
import com.finapp.feature.common.utils.toFormattedString
import java.math.BigDecimal
import java.math.RoundingMode

fun List<Transaction>.toAnalysisItems(): List<ExpensesAnalysisItemUiState> {
    val sumByCategory: Map<Category, BigDecimal> =
        groupBy { it.category }
            .mapValues { (_, transactions) ->
                transactions.sumOf { it.amount }
            }

    val totalAmount: BigDecimal = sumByCategory.values
        .fold(BigDecimal.ZERO) { accumulator, amount -> accumulator + amount }
    if (totalAmount == BigDecimal.ZERO) return emptyList()

    data class CategorySumPercentage(
        val category: Category,
        val sum: BigDecimal,
        val exactPercentage: BigDecimal
    )

    val categoryPercentages: List<CategorySumPercentage> = sumByCategory.map { (category, sum) ->
        val exact = sum
            .multiply(BigDecimal(100))
            .divide(totalAmount, 10, RoundingMode.HALF_UP)
        CategorySumPercentage(category, sum, exact)
    }

    val floorPercentages: List<Int> = categoryPercentages.map { it.exactPercentage
        .setScale(0, RoundingMode.DOWN)
        .toInt()
    }
    val fractionalPercentages: List<BigDecimal> = categoryPercentages.map { it.exactPercentage
        .subtract(BigDecimal(it.exactPercentage.setScale(0, RoundingMode.DOWN).toInt()))
    }

    val sumOfFloors = floorPercentages.sum()
    val unitsToDistribute = 100 - sumOfFloors

    val sortedIndicesByFraction: List<Int> = fractionalPercentages
        .mapIndexed { index, fraction -> index to fraction }
        .sortedByDescending { it.second }
        .map { it.first }

    val adjustedPercentages = MutableList(floorPercentages.size) { floorPercentages[it] }

    for (i in 0 until unitsToDistribute.coerceAtLeast(0).coerceAtMost(floorPercentages.size)) {
        val categoryIndex = sortedIndicesByFraction[i]
        adjustedPercentages[categoryIndex] = adjustedPercentages[categoryIndex] + 1
    }

    return categoryPercentages.mapIndexed { index, entry ->
        val rawPercent = adjustedPercentages[index]
        val percentString = if (rawPercent <= 0) "<1" else rawPercent.toString()

        ExpensesAnalysisItemUiState(
            categoryId = entry.category.id,
            leadingSymbols = entry.category.emoji,
            title = entry.category.name,
            amount = entry.sum.toFormattedString(),
            percent = percentString
        )
    }
}