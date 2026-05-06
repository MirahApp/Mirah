package mirah.extensions

import java.io.File
import java.net.URLClassLoader
import java.util.zip.ZipFile

data class LoadedExtension(
    val packageName: String,
    val name: String,
    val versionName: String,
    val sources: List<Any>,
    val classLoader: URLClassLoader
)

object ExtensionLoader {
    fun loadFromJar(jarFile: File, packageName: String, versionName: String): LoadedExtension? {
        val classLoader = URLClassLoader(
            arrayOf(jarFile.toURI().toURL()),
            Thread.currentThread().contextClassLoader
        )

        val sources = mutableListOf<Any>()

        try {
            ZipFile(jarFile).use { zip ->
                zip.entries().asSequence()
                    .filter { it.name.endsWith(".class") && !it.name.contains("$") }
                    .forEach { entry ->
                        val className = entry.name
                            .replace("/", ".")
                            .removeSuffix(".class")

                        if (className.startsWith("eu.kanade.tachiyomi.extension")) {
                            try {
                                val clazz = classLoader.loadClass(className)
                                val constructor = clazz.getDeclaredConstructor()
                                constructor.isAccessible = true
                                val instance = constructor.newInstance()
                                sources.add(instance)
                            } catch (e: Exception) {
                                // Skip individual failures
                            }
                        }
                    }
            }
        } catch (e: Exception) {
            return null
        }

        if (sources.isEmpty()) return null

        return LoadedExtension(
            packageName = packageName,
            name = packageName,
            versionName = versionName,
            sources = sources,
            classLoader = classLoader
        )
    }

    fun loadAllFromDirectory(dir: File): List<LoadedExtension> {
        if (!dir.exists() || !dir.isDirectory) return emptyList()

        return dir.listFiles { file -> file.extension == "jar" }
            ?.mapNotNull { jarFile ->
                loadFromJar(
                    jarFile = jarFile,
                    packageName = jarFile.nameWithoutExtension,
                    versionName = "unknown"
                )
            } ?: emptyList()
    }
}
