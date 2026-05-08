package mirah.extensions

import eu.kanade.tachiyomi.source.Source
import eu.kanade.tachiyomi.source.SourceFactory
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
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
    private val cache = mutableMapOf<String, LoadedExtension?>()

    fun loadFromJar(jarFile: File, packageName: String, versionName: String): LoadedExtension? {
        val cacheKey = "${jarFile.absolutePath}:${jarFile.lastModified()}:${jarFile.length()}"
        synchronized(cache) {
            if (cache.containsKey(cacheKey)) return cache[cacheKey]
        }

        val classLoader = URLClassLoader(
            arrayOf(jarFile.toURI().toURL()),
            Thread.currentThread().contextClassLoader
        )

        val sources = mutableListOf<Any>()
        val failuresByType = linkedMapOf<String, Int>()
        var scannedClassCount = 0
        var skippedNoCtorCount = 0

        fun trackFailure(t: Throwable) {
            val key = when (t) {
                is InvocationTargetException -> {
                    val c = t.cause
                    if (c != null) "InvocationTargetException(${c.javaClass.simpleName})" else "InvocationTargetException"
                }
                else -> t.javaClass.simpleName
            }
            failuresByType[key] = (failuresByType[key] ?: 0) + 1
        }

        try {
            ZipFile(jarFile).use { zip ->
                zip.entries().asSequence()
                    .filter { it.name.endsWith(".class") && !it.name.contains("$") }
                    .forEach { entry ->
                        val className = entry.name
                            .replace("/", ".")
                            .removeSuffix(".class")

                        if (!className.startsWith("eu.kanade.tachiyomi.extension")) return@forEach
                        scannedClassCount++

                        try {
                            val clazz = classLoader.loadClass(className)
                            if (clazz.isInterface || Modifier.isAbstract(clazz.modifiers)) return@forEach

                            when {
                                SourceFactory::class.java.isAssignableFrom(clazz) -> {
                                    val instance = instantiateExtensionClass(clazz) as? SourceFactory
                                    if (instance == null) {
                                        skippedNoCtorCount++
                                        return@forEach
                                    }
                                    val createdSources = instance.createSources()
                                    sources.addAll(createdSources)
                                }
                                Source::class.java.isAssignableFrom(clazz) -> {
                                    val instance = instantiateExtensionClass(clazz)
                                    if (instance == null) {
                                        skippedNoCtorCount++
                                        return@forEach
                                    }
                                    sources.add(instance)
                                }
                            }
                        } catch (e: Throwable) {
                            trackFailure(e)
                            ExtensionDiagnostics.recordFailure(jarFile.name, className, e)
                        }
                    }
            }
        } catch (e: Throwable) {
            synchronized(cache) { cache[cacheKey] = null }
            return null
        }

        if (failuresByType.isNotEmpty()) {
            val summary = failuresByType.entries.joinToString(", ") { "${it.key}=${it.value}" }
            println("[Mirah] ${jarFile.name}: scanned=$scannedClassCount loaded=${sources.size} skippedNoCtor=$skippedNoCtorCount failures={$summary}")
        }

        if (sources.isEmpty()) {
            synchronized(cache) { cache[cacheKey] = null }
            return null
        }

        val loaded = LoadedExtension(
            packageName = packageName,
            name = packageName,
            versionName = versionName,
            sources = sources,
            classLoader = classLoader
        )
        synchronized(cache) { cache[cacheKey] = loaded }
        return loaded
    }

    fun loadAllFromDirectory(dir: File): List<LoadedExtension> {
        if (!dir.exists() || !dir.isDirectory) return emptyList()

        return dir.listFiles { file -> file.extension == "jar" }
            ?.mapNotNull { jarFile ->
                try {
                    loadFromJar(
                        jarFile = jarFile,
                        packageName = jarFile.nameWithoutExtension,
                        versionName = "unknown"
                    )
                } catch (_: Throwable) {
                    null
                }
            } ?: emptyList()
    }

    private fun instantiateExtensionClass(clazz: Class<*>): Any? {
        // Kotlin object singleton support: class exposes public static INSTANCE field.
        try {
            val instanceField = clazz.getField("INSTANCE")
            instanceField.isAccessible = true
            instanceField.get(null)?.let { return it }
        } catch (_: Throwable) {
        }

        return try {
            val constructor = clazz.getDeclaredConstructor()
            constructor.isAccessible = true
            constructor.newInstance()
        } catch (_: NoSuchMethodException) {
            null
        }
    }
}
