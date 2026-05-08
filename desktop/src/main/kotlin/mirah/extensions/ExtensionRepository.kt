package mirah.extensions

import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class RemoteExtension(
    val name: String,
    val packageName: String,
    val apkFileName: String,
    val lang: String,
    val versionName: String,
    val isNsfw: Boolean,
    val downloadUrl: String
)

object ExtensionRepository {

    private const val indexUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.min.json"
    private const val downloadBaseUrl = "https://raw.githubusercontent.com/keiyoushi/extensions/repo/apk/"

    fun fetchExtensions(): List<RemoteExtension> {
        return try {
            val connection = URL(indexUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 15000
            connection.setRequestProperty("User-Agent", "Mirah/1.0")

            val response = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            // Manual JSON parsing
            val jsonArray = response.trim().removeSurrounding("[", "]")
            val objects = jsonArray.split("},{")

            objects.mapNotNull { obj ->
                val name = extractField(obj, "name")
                val pkg = extractField(obj, "pkg")
                val apk = extractField(obj, "apk")
                val lang = extractField(obj, "lang")
                val version = extractField(obj, "version")
                val nsfwRaw = extractField(obj, "nsfw")
                val isNsfw = (nsfwRaw.toIntOrNull() ?: 0) == 1

                if (pkg.isBlank() || apk.isBlank()) return@mapNotNull null

                RemoteExtension(
                    name = name,
                    packageName = pkg,
                    apkFileName = apk,
                    lang = lang,
                    versionName = version,
                    isNsfw = isNsfw,
                    downloadUrl = "$downloadBaseUrl$apk"
                )
            }.sortedBy { it.name }

        } catch (e: Exception) {
            println("[Mirah] Failed to fetch extension list: ${e.message}")
            emptyList()
        }
    }

   fun downloadApk(extension: RemoteExtension, destDir: File): File {
        destDir.mkdirs()
        val destFile = File(destDir, extension.apkFileName)

        try {
            var url = URL(extension.downloadUrl)
            var connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 30000
            connection.setRequestProperty("User-Agent", "Mirah/1.0")
            connection.instanceFollowRedirects = true

            // Follow redirects manually if needed
            var responseCode = connection.responseCode
            while (responseCode == HttpURLConnection.HTTP_MOVED_TEMP ||
                responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                responseCode == 307 || responseCode == 308) {
                val newUrl = connection.getHeaderField("Location")
                connection.disconnect()
                url = URL(newUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 10000
                connection.readTimeout = 30000
                connection.setRequestProperty("User-Agent", "Mirah/1.0")
                responseCode = connection.responseCode
            }

            connection.inputStream.use { input ->
                destFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            connection.disconnect()
            return destFile
        } catch (e: Exception) {
            throw RuntimeException("Failed to download ${extension.name}", e)
        }
    }

    private fun extractField(json: String, key: String): String {
        val searchKey = "\"$key\":"
        val keyIndex = json.indexOf(searchKey)
        if (keyIndex == -1) return ""

        val valueStart = keyIndex + searchKey.length
        val substring = json.substring(valueStart).trim()

        return if (substring.startsWith("\"")) {
            // String value
            val endQuote = substring.indexOf("\"", 1)
            if (endQuote != -1) substring.substring(1, endQuote) else ""
        } else {
            // Numeric or unquoted value
            val delimiterIndex = substring.indexOfAny(charArrayOf(',', '}', ']'))
            if (delimiterIndex != -1) substring.substring(0, delimiterIndex).trim() else substring.trim()
        }
    }
}
