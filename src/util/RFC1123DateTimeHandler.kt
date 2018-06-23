package util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object RFC1123DateTimeHandler {
    public val GMT : ZoneId = ZoneId.of("GMT")
    private val RFC_1123_FORMATTER : DateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME

    fun nowDateTimeStr() : String {
        val now : ZonedDateTime = ZonedDateTime.now(GMT)
        return now.format(RFC_1123_FORMATTER)
    }

    fun parseZonedDateTime(zonedDateTime : ZonedDateTime): String {
        if (zonedDateTime.getZone() == GMT) {
            return zonedDateTime.format(RFC_1123_FORMATTER)
        } else return zonedDateTime.withZoneSameInstant(GMT).format(RFC_1123_FORMATTER)
    }

    fun parseStrDateTime(strDateTime : String): ZonedDateTime? {
        try {
            val localDateTime : LocalDateTime = LocalDateTime.parse(strDateTime, RFC_1123_FORMATTER)
            return ZonedDateTime.of(localDateTime, GMT)
        } catch(e : DateTimeParseException) {
            return null
        }
    }
}