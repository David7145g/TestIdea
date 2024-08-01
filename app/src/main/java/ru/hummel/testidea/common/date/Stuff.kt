package ru.hummel.testidea.common.date

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


fun epochMillisToLocalDateTime(epochMillis: Long): LocalDateTime {
  val instant = Instant.ofEpochMilli(epochMillis)
  return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}

fun localDateTimeToEpochMillis(localDateTime: LocalDateTime): Long {
  val zonedDateTime = localDateTime.atZone(ZoneId.systemDefault())
  return zonedDateTime.toInstant().toEpochMilli()
}

fun LocalDateTime.toMyFormat(): String {
  val defaultPattern = "dd.MM.yyyy"
  val formatter = DateTimeFormatter.ofPattern(defaultPattern)
  return this.format(formatter)
}