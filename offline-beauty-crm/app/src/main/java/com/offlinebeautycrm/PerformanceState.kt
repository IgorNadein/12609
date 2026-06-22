package com.offlinebeautycrm

import java.time.LocalDate
import java.time.LocalDateTime

data class ParsedAppointmentRow(
    val appointment: AppointmentRow,
    val start: LocalDateTime
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
