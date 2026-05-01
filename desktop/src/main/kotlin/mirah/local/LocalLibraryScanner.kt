package mirah.local

import java.io.File
import java.util.zip.ZipFile

object LocalLibraryScanner {
    fun scanFolder(folder: File): List<LocalManga> {
        if (!folder.exists() || !folder.isDirectory) return emptyList()

        val mangaFiles = folder.listFiles { file ->
            val ext = file.extension.lowercase()
            file.isFile && (ext == "cbz" || ext == "zip")
        } ?: return emptyList()

        return mangaFiles.mapNotNull { file ->
            try {
                ZipFile(file).use { zip ->
                    val coverEntry = zip.entries().asSequence()
                        .filter { entry ->
                            val name = entry.name.lowercase()
                            !entry.isDirectory && (
                                name.endsWith(".jpg") || 
                                name.endsWith(".jpeg") || 
                                name.endsWith(".png") || 
                                name.endsWith(".webp")
                            )
                        }
                        .sortedBy { it.name }
                        .firstOrNull()

                    val coverBytes = coverEntry?.let { entry ->
                        zip.getInputStream(entry).use { it.readBytes() }
                    }

                    LocalManga(
                        title = file.nameWithoutExtension,
                        file = file,
                        coverImage = coverBytes
                    )
                }
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.title }
    }
}
