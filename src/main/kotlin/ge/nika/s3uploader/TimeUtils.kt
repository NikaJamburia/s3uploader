package ge.nika.s3uploader

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

private val utc = ZoneId.of("UTC")

fun Instant.toUtc(): Instant = this.atZone(utc).toInstant()

fun String.toUtcInstant() = LocalDateTime.parse(this)
    .atZone(utc)
    .toInstant()