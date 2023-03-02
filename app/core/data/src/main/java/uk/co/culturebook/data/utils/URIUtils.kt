package uk.co.culturebook.data.utils

import android.net.Uri
import java.net.URI

fun URI.toUri(): Uri = Uri.parse(toString())

fun Uri.isValidContent() = "content.*".toRegex().matches(scheme ?: "")

fun Uri.isValidHttp() = "http.*".toRegex().matches(scheme ?: "")