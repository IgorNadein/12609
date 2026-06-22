package com.offlinebeautycrm

import java.time.LocalDate
import java.time.LocalDateTime

data class ParsedAppointmentRow(
    val appointment: AppointmentRow,
    val start: LocalDateTime
)

data class ParsedDayOffRow(
    val dayOff: DayOffEntity,
    val start: LocalDateTime,
    val end: LocalDateTime
)

data class AppointmentIndex(
    val appointments: List<AppointmentRow> = emptyList(),
    val parsed: List<ParsedAppointmentRow> = emptyList(),
    val byDate: Map<LocalDate, List<AppointmentRow>> = emptyMap(),
    val parsedByDate: Map<LocalDate, List<ParsedAppointmentRow>> = emptyMap(),
    val byClientId: Map<Long, List<AppointmentRow>> = emptyMap(),
    val byId: Map<Long, AppointmentRow> = emptyMap()
) {
    companion object {
        val Empty = AppointmentIndex()
    }
}

data class DayOffIndex(
    val dayOffs: List<DayOffEntity> = emptyList(),
    val parsed: List<ParsedDayOffRow> = emptyList(),
    val byDate: Map<LocalDate, List<DayOffEntity>> = emptyMap(),
    val parsedByDate: Map<LocalDate, List<ParsedDayOffRow>> = emptyMap(),
    val byId: Map<Long, DayOffEntity> = emptyMap()
) {
    companion object {
        val Empty = DayOffIndex()
    }
}

fun buildAppointmentIndex(appointments: List<AppointmentRow>): AppointmentIndex {
    val parsed = appointments.mapNotNull { appointment ->
        parseAppointmentDateTime(appointment.startAt)?.let { start ->
            ParsedAppointmentRow(appointment = appointment, start = start)
        }
    }.sortedBy { it.start }
    val parsedIds = parsed.map { it.appointment.id }.toSet()
    val parsedByClientId = parsed
        .groupBy { it.appointment.clientId }
        .mapValues { entry -> entry.value.map { it.appointment } }
    val unparsedByClientId = appointments
        .filter { it.id !in parsedIds }
        .groupBy { it.clientId }
    val byClientId = (parsedByClientId.keys + unparsedByClientId.keys).associateWith { clientId ->
        parsedByClientId[clientId].orEmpty() + unparsedByClientId[clientId].orEmpty()
    }
    return AppointmentIndex(
        appointments = appointments,
        parsed = parsed,
        byDate = parsed
            .groupBy { it.start.toLocalDate() }
            .mapValues { entry -> entry.value.map { it.appointment } },
        parsedByDate = parsed.groupBy { it.start.toLocalDate() },
        byClientId = byClientId,
        byId = appointments.associateBy { it.id }
    )
}

fun buildDayOffIndex(dayOffs: List<DayOffEntity>): DayOffIndex {
    val parsed = dayOffs.mapNotNull { dayOff ->
        val start = parseAppointmentDateTime(dayOff.startAt) ?: return@mapNotNull null
        val end = parseAppointmentDateTime(dayOff.endAt) ?: return@mapNotNull null
        if (!end.isAfter(start)) return@mapNotNull null
        ParsedDayOffRow(dayOff = dayOff, start = start, end = end)
    }.sortedBy { it.start }
    val parsedByDateRows = parsed.flatMap { row ->
        buildList {
            var date = row.start.toLocalDate()
            val endDate = row.end.toLocalDate()
            while (!date.isAfter(endDate)) {
                val segmentStart = if (date == row.start.toLocalDate()) row.start else date.atStartOfDay()
                val segmentEnd = if (date == endDate) row.end else date.plusDays(1).atStartOfDay()
                if (segmentEnd.isAfter(segmentStart)) {
                    add(ParsedDayOffRow(dayOff = row.dayOff, start = segmentStart, end = segmentEnd))
                }
                date = date.plusDays(1)
            }
        }
    }.sortedBy { it.start }
    return DayOffIndex(
        dayOffs = dayOffs,
        parsed = parsed,
        byDate = parsedByDateRows
            .groupBy { it.start.toLocalDate() }
            .mapValues { entry -> entry.value.map { it.dayOff }.distinctBy { dayOff -> dayOff.id } },
        parsedByDate = parsedByDateRows.groupBy { it.start.toLocalDate() },
        byId = dayOffs.associateBy { it.id }
    )
}
