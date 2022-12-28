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

sealed interface SharedPrefs {
    val key: String

    object UserSession : SharedPrefs {
        override val key: String = "user_session"
    }
}