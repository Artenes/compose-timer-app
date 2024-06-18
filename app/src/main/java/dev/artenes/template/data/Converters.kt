package dev.artenes.template.data

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class Converters {

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): String? {
        return localDateTime?.toString()
    }

    @TypeConverter
    fun stringToLocalDateTime(string: String?): LocalDateTime? {
        if (string == null) {
            return null
        }
        return LocalDateTime.parse(string)
    }

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): String? {
        return localDate?.toString()
    }

    @TypeConverter
    fun stringToLocalDate(string: String?): LocalDate? {
        if (string == null) {
            return null
        }
        return LocalDate.parse(string)
    }

    @TypeConverter
    fun fromOffset(offset: OffsetDateTime?): Long? {
        return offset?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun longToOffset(long: Long?): OffsetDateTime? {
        if (long == null) {
            return null
        }
        return Instant.ofEpochMilli(long).atOffset(OffsetDateTime.now().offset)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(value: String?): UUID? {
        if (value == null) {
            return null
        }
        return UUID.fromString(value)
    }

}