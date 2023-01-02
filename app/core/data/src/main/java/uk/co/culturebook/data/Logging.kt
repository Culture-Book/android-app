package uk.co.culturebook.data

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

fun String?.logD(tag: String = "DEBUG") {
    if (BuildConfig.DEBUG) {
        Log.d(tag, this ?: "null Message")
    } else {
        FirebaseCrashlytics.getInstance().log("$tag: ${this ?: "WARNING logged but was null"}")
    }
}

fun String?.logE(tag: String = "ERROR") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, this ?: "null")
    } else {
        FirebaseCrashlytics.getInstance().log("$tag: ${this ?: "ERROR logged but was null"}")
    }
}

fun Throwable?.logE(tag: String = "EXCEPTION") {
    if (BuildConfig.DEBUG) {
        Log.e(tag, this?.stackTraceToString() ?: "EXCEPTION THROWN but was null")
    } else {
        FirebaseCrashlytics.getInstance()
            .log("$tag: ${this?.stackTraceToString() ?: "EXCEPTION logged but was null"}")
    }
}