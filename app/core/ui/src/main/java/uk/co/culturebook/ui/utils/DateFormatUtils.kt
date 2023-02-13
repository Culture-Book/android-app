package uk.co.culturebook.ui.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun LocalDateTime.prettyPrint(): String =
    DateTimeFormatter.ofPattern("dd MMM yy - HH:mm").format(this)