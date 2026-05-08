package eu.kanade.tachiyomi.network.interceptor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.HttpUrl
import java.util.concurrent.TimeUnit

@Suppress("unused")
fun specificHostRateLimitInterceptor(host: String): Interceptor {
    return Interceptor { chain -> chain.proceed(chain.request()) }
}

@Suppress("unused")
fun specificHostRateLimitInterceptor(): Interceptor {
    return Interceptor { chain -> chain.proceed(chain.request()) }
}

@Suppress("unused")
fun OkHttpClient.Builder.rateLimitHost(
    host: HttpUrl,
    permits: Int = 1,
    period: Long = 1,
    unit: TimeUnit = TimeUnit.SECONDS
): OkHttpClient.Builder = this
