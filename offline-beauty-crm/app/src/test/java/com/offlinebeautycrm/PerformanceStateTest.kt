package com.offlinebeautycrm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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

    @Test
    fun financeSearchMatchesAnyCommaSeparatedQuery() {
        val podology = FinanceJournalItem(
            key = "transaction-1",
            kind = "income",
            title = "Подология",
            subtitle = "Услуги · Карта",
            amountCents = 5_000,
            date = "2026-09-29"
        )

        assertTrue(financeJournalItemMatches(podology, "Все", "подология"))
        assertTrue(financeJournalItemMatches(podology, "Все", "наращивание, подология"))
        assertFalse(financeJournalItemMatches(podology, "Все", "маникюр, наращивание"))
    }

    @Test
    fun financeSearchIgnoresEmptyCommaSeparatedQueries() {
        assertEquals(listOf("подология", "наращивание"), financeSearchQueries(" подология, , наращивание "))
        assertEquals(emptyList<String>(), financeSearchQueries(" , "))
    }

    @Test
    fun financeSummaryUsesOnlyProvidedJournalItems() {
        val income = FinanceJournalItem("income", "income", "Подология", "", 5_000, "2026-09-29")
        val expense = FinanceJournalItem("expense", "expense", "Материалы", "", 1_200, "2026-09-29")
        val debt = FinanceJournalItem("debt", "debt", "Наращивание", "", 2_000, "2026-09-29", isDebt = true)

        val summary = financeSummaryForJournalItems(listOf(income, expense, debt))

        assertEquals(5_000L, summary.paidCents)
        assertEquals(1_200L, summary.expenseCents)
        assertEquals(2_000L, summary.debtCents)
        assertEquals(3_800L, summary.totalCents)
    }

    @Test
    fun clientSearchCoversAlternateFieldsAndFormattedPhones() {
        val client = ClientEntity(
            id = 7,
            name = "Анна Иванова",
            nickname = "Анюта",
            phone = "+7 (999) 123-45-67",
            phoneWork = "+7 495 765-43-21",
            emailWork = "anna@studio.example",
            company = "Студия красоты"
        )

        assertTrue(client.matchesClientSearch("Анюта"))
        assertTrue(client.matchesClientSearch("495765"))
        assertTrue(client.matchesClientSearch("8 999 123"))
        assertTrue(client.matchesClientSearch("anna@studio"))
        assertTrue(client.matchesClientSearch("Анна студия"))
        assertFalse(client.matchesClientSearch("Мария салон"))
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
