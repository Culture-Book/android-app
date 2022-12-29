package io.culturebook.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

private const val sharedPrefsFile: String = "culturebook"
private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

val Context.sharedPreferences: SharedPreferences
    get() = EncryptedSharedPreferences.create(
        sharedPrefsFile,
        mainKeyAlias,
        applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

inline fun <reified T : Any?> SharedPreferences.put(prefKey: PrefKey, value: T) {
    when (value) {
        is Int -> edit().putInt(prefKey.key, value).apply()
        is Float -> edit().putFloat(prefKey.key, value).apply()
        is Long -> edit().putLong(prefKey.key, value).apply()
        is String -> edit().putString(prefKey.key, value).apply()
        is Boolean -> edit().putBoolean(prefKey.key, value).apply()
        is Set<*> -> edit().putStringSet(prefKey.key, value.filterIsInstance<String>().toSet())
            .apply()
    }
}

sealed interface PrefKey {
    val key: String

    object UserSession : PrefKey {
        override val key: String = "user_session"
    }
}