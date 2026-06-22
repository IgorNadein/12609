package com.offlinebeautycrm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.time.LocalDate

class PerformanceStateTest {
    @Test
    fun parseAppointmentDateTimeSupportsMinuteAndSecondFormats() {
        assertEquals(10, parseAppointmentDateTime("2026-06-22 10:15")?.hour)
        assertEquals(15, parseAppointmentDateTime("2026-06-22 10:15")?.minute)
        assertEquals(30, parseAppointmentDateTime("2026-06-22 10:15:30")?.second)
        assertNotNull(parseAppointmentDateTime("2026-06-22 10:15:30"))
    }

    @Test
    fun buildAppointmentIndexGroupsByDateAndClient() {
        val first = appointmentRow(id = 1, clientId = 10, startAt = "2026-06-22 09:00")
        val second = appointmentRow(id = 2, clientId = 10, startAt = "2026-06-22 11:00")
        val third = appointmentRow(id = 3, clientId = 20, startAt = "2026-06-23 10:00")

        val index = buildAppointmentIndex(listOf(third, second, first))

        assertEquals(listOf(first, second), index.byDate[LocalDate.of(2026, 6, 22)])
        assertEquals(listOf(first, second), index.byClientId[10])
        assertEquals(third, index.byId[3])
    }

    private fun appointmentRow(
        id: Long,
        clientId: Long,
        startAt: String
    ): AppointmentRow = AppointmentRow(
        id = id,
        clientId = clientId,
        service = "service-$id",
        clientName = "client-$clientId",
        startAt = startAt,
        durationMinutes = 60,
        price = "",
        priceCents = 0,
        paidAmountCents = 0,
        paymentStatus = "unpaid",
        paymentMethod = "",
        paidAt = "",
        status = "Запланирована",
        notes = "",
        calendarEventId = 0,
        calendarSyncedAt = "",
        updatedAt = ""
    )
}
