package eu.kanade.tachiyomi.network

import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class NetworkHelper {
    val client: OkHttpClient = OkHttpClient.Builder().build()
    val cloudflareClient: OkHttpClient = OkHttpClient.Builder().build()

    fun defaultUserAgentProvider(): String = "Mirah/desktop"

    fun newCachelessCallWithProgress(request: Request, page: Int): Call {
        return client.newCall(request)
    }
}