package com.noniboy.struja.domain.validation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReadingValidatorTest {

    @Test
    fun `validateDate returns date for valid input`() {
        val date = ReadingValidator.validateDate("2024-01-15")
        assertNotNull(date)
        assertEquals(2024, date.year)
        assertEquals(1, date.monthValue)
        assertEquals(15, date.dayOfMonth)
    }

    @Test
    fun `validateDate throws for future date`() {
        val futureDate = LocalDate.now().plusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateDate(futureDate)
        }
    }

    @Test
    fun `validateDate throws for invalid format`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateDate("15-01-2024")
        }
    }

    @Test
    fun `validateDate throws when not after previous reading`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateDate("2024-01-15", "2024-01-20")
        }
    }

    @Test
    fun `validateDate accepts date after previous reading`() {
        val date = ReadingValidator.validateDate("2024-01-25", "2024-01-20")
        assertNotNull(date)
    }

    @Test
    fun `validateReadings throws for negative VT`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateReadings(-1, 100)
        }
    }

    @Test
    fun `validateReadings throws for negative MT`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateReadings(100, -1)
        }
    }

    @Test
    fun `validateReadings throws for VT lower than previous`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateReadings(90, 100, prevVt = 100, prevMt = 90)
        }
    }

    @Test
    fun `validateReadings throws for MT lower than previous`() {
        assertThrows(ReadingValidationException::class.java) {
            ReadingValidator.validateReadings(100, 90, prevVt = 90, prevMt = 100)
        }
    }

    @Test
    fun `validateReadings accepts valid readings`() {
        ReadingValidator.validateReadings(150, 120, prevVt = 100, prevMt = 80)
    }
}
