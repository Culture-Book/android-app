package uk.co.culturebook.common

import android.content.Context
import android.content.pm.PackageManager

fun Context.getVersionName(): String? {
    val packageInfo = try {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        return null
    }

    return packageInfo?.versionName
}