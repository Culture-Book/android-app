package uk.co.culturebook.data.utils

import java.util.UUID

fun String?.toUUID() = this?.let { UUID.fromString(it) }