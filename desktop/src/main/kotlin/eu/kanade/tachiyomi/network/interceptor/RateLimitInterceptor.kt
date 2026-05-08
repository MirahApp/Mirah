package eu.kanade.tachiyomi.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class RateLimitInterceptor(
    private val permits: Int = 1,
    private val period: Long = 1
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.proceed(chain.request())
}

fun okhttp3.OkHttpClient.Builder.rateLimit(
    permits: Int = 1,
    period: Long = 1,
    unit: java.util.concurrent.TimeUnit = java.util.concurrent.TimeUnit.SECONDS
) = this