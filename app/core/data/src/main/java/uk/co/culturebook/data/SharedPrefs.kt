package uk.co.culturebook.data

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

fun SharedPreferences.remove(prefKey: PrefKey) = edit().remove(prefKey.key).apply()

sealed interface PrefKey {
    val key: String

    object AccessToken : PrefKey {
        override val key: String = "access_token"
    }

    object RefreshToken : PrefKey {
        override val key: String = "refresh_token"
    }

    object Element : PrefKey {
        override val key = "element"
    }

    object Contribution : PrefKey {
        override val key = "contribution"
    }

    object Files : PrefKey {
        override val key = "files"
    }

    object User : PrefKey {
        override val key = "user"
    }

    object PublicKey : PrefKey {
        override val key = "public_key"
    }

    object MaterialYou : PrefKey {
        override val key = "material_you"
    }
}