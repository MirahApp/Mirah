package eu.kanade.tachiyomi.source.online

import eu.kanade.tachiyomi.network.NetworkHelper
import eu.kanade.tachiyomi.source.CatalogueSource
import okhttp3.Headers
import okhttp3.Headers.Companion.headersOf
import okhttp3.OkHttpClient

abstract class HttpSource : CatalogueSource {
    protected val networkHelper = NetworkHelper()
    open val versionId: Int = 1
    open val client: OkHttpClient
        get() = networkHelper.client
    abstract val baseUrl: String
    val headers: Headers by lazy { headersBuilder().build() }
    override val lang: String = "all"
    override val id: Long get() = name.hashCode().toLong()

    protected open fun headersBuilder() = Headers.Builder().apply {
        add("User-Agent", networkHelper.defaultUserAgentProvider())
    }

    fun getNetwork(): NetworkHelper = networkHelper
}