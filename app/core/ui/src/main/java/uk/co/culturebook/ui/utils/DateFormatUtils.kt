package uk.co.culturebook.ui.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.prettyPrint(): String =
    DateTimeFormatter.ofPattern("dd MMM yy - HH:mm").format(this)