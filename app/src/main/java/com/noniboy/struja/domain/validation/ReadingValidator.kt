package com.noniboy.struja.domain.validation

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ReadingValidationException(message: String) : Exception(message)

object ReadingValidator {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun validateDate(
        recordedAt: String,
        previousRecordedAt: String? = null
    ): LocalDate {
        val date = parseDateOnly(recordedAt)
            ?: throw ReadingValidationException("The reading date is invalid.")

        val today = LocalDate.now()
        if (date.isAfter(today)) {
            throw ReadingValidationException("The reading date is invalid.")
        }

        if (previousRecordedAt != null && recordedAt <= previousRecordedAt) {
            throw ReadingValidationException("The reading date must be later than the previous reading.")
        }

        return date
    }

    fun validateReadings(
        vt: Int,
        mt: Int,
        prevVt: Int? = null,
        prevMt: Int? = null
    ) {
        if (vt < 0 || mt < 0) {
            throw ReadingValidationException("Both readings must be non-negative numbers.")
        }

        if (prevVt != null && vt < prevVt) {
            throw ReadingValidationException("A reading cannot be lower than the previous reading.")
        }

        if (prevMt != null && mt < prevMt) {
            throw ReadingValidationException("A reading cannot be lower than the previous reading.")
        }
    }

    private fun parseDateOnly(value: String): LocalDate? {
        return try {
            val date = LocalDate.parse(value, dateFormatter)
            if (date.format(dateFormatter) == value) date else null
        } catch (e: DateTimeParseException) {
            null
        }
    }
}
