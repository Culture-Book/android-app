package uk.co.culturebook.data.remote_config

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import uk.co.culturebook.data.R

fun getRemoteConfig(activity: Activity): FirebaseRemoteConfig =
    Firebase.remoteConfig.apply {
        setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        setDefaultsAsync(R.xml.remote_config_defaults)
        fetchAndActivate().addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Config params updated: $updated")
            }
        }
    }

sealed interface RemoteConfig {
    val key: String

    object ToSUrl : RemoteConfig {
        override val key: String = "tos_url"
    }

    object PrivacyUrl : RemoteConfig {
        override val key: String = "privacy_url"
    }

    object GoogleKey : RemoteConfig {
        override val key: String = "google_key"
    }
}