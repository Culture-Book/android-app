package io.culturebook.data

import android.util.Log

fun String?.logD(tag: String = "DEBUG") =
    if (BuildConfig.DEBUG) Log.d(tag, this ?: "null Message") else null