package uk.co.culturebook.data.utils

import android.net.Uri
import java.net.URI

fun URI.toUri() = Uri.parse(toString())