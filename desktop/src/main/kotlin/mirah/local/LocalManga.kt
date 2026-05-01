package mirah.local

import java.io.File

data class LocalManga(
    val title: String,
    val file: File,
    val coverImage: ByteArray? = null
)
