package uk.co.culturebook.data

import android.content.Context
import androidx.annotation.RestrictTo
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import coil.ImageLoader
import coil.disk.DiskCache
import coil.imageLoader
import coil.memory.MemoryCache
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.remote.retrofit.getAuthenticatedRetrofitClient
import uk.co.culturebook.data.remote.retrofit.getAuthenticationRetrofitClient
import uk.co.culturebook.data.remote.retrofit.imageLoaderClient

object Singletons {
    private var authInterface: AuthInterface? = null
    private var apiInterface: ApiInterface? = null
    private var videoCache: SimpleCache? = null
    private var imageLoader: ImageLoader? = null

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun getVideoCache(context: Context): SimpleCache =
        videoCache.let {
            if (it == null) {
                SimpleCache(
                    context.cacheDir,
                    NoOpCacheEvictor(),
                    StandaloneDatabaseProvider(context)
                ).also { cache ->
                    videoCache = cache
                    return cache
                }
            } else {
                return it
            }
        }

    @androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
    fun releaseVideoCache() {
        videoCache?.release()
        videoCache = null
    }

    fun getImageLoader(context: Context, errorImages: List<Int>): ImageLoader {
        fun createImageLoader(): ImageLoader {
            return try {
                context.imageLoader.newBuilder()
                    .okHttpClient(imageLoaderClient)
                    .crossfade(true)
                    .error(errorImages.random())
                    .memoryCache {
                        MemoryCache.Builder(context)
                            .maxSizePercent(0.02)
                            .build()
                    }
                    .diskCache {
                        DiskCache.Builder()
                            .directory(context.cacheDir.resolve("image_cache"))
                            .maxSizePercent(0.02)
                            .build()
                    }
                    .build()
            } catch (e: Exception) {
                e.logE()
                context.imageLoader
                    .newBuilder()
                    .okHttpClient(imageLoaderClient)
                    .crossfade(true)
                    .error(errorImages.random())
                    .build()
            }
        }

        imageLoader.let {
            if (it == null) {
                createImageLoader().also { loader ->
                    imageLoader = loader
                    return loader
                }
            } else {
                return it
            }
        }
    }

    fun releaseImageLoader() {
        imageLoader?.shutdown()
        imageLoader = null
    }

    fun getAuthInterface(): AuthInterface =
        authInterface.let {
            if (it == null) {
                authInterface = getAuthenticationRetrofitClient()
                return authInterface!!
            } else {
                return it
            }
        }

    fun getApiInterface(context: Context): ApiInterface =
        apiInterface.let {
            if (it == null) {
                apiInterface = getAuthenticatedRetrofitClient(context)
                return apiInterface!!
            } else {
                return it
            }
        }

    @RestrictTo(RestrictTo.Scope.TESTS)
    fun setMockInterfaces(authInterface: AuthInterface, apiInterface: ApiInterface) {
        this.authInterface = authInterface
        this.apiInterface = apiInterface
    }

    fun resetInterfaces() {
        authInterface = null
        apiInterface = null
    }
}