package uk.co.culturebook.data.utils

import java.util.*

fun String?.toUUID() = this?.let { UUID.fromString(it) }