package com.finapp.core.domain.time

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * Утилиты для конвертации [LocalDate] в ISO-OffsetDateTime в UTC — в таком виде Room хранит
 * границы периодов транзакций (`transaction_date_iso BETWEEN :startIso AND :endIso`).
 *
 * Сетевой API принимает «голую» дату вида `yyyy-MM-dd` — для него используется `start.toString()`,
 * не эти функции. Эти — только для локального источника.
 */

/** `yyyy-MM-ddT00:00:00+00:00` — нижняя граница суток в UTC. */
fun LocalDate.atStartOfDayIsoUtc(): String =
    atStartOfDay()
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

/** `yyyy-MM-ddT23:59:59.999999999+00:00` — верхняя граница суток в UTC. */
fun LocalDate.atEndOfDayIsoUtc(): String =
    LocalDateTime.of(this, LocalTime.MAX)
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
