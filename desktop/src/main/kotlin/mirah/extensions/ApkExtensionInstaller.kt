package mirah.extensions

import java.io.File

object ApkExtensionInstaller {

    private val extensionsDir: File
        get() = File(System.getProperty("user.home"), ".mirah/extensions").also { it.mkdirs() }

    fun install(apkFile: File): LoadedExtension {
        println("[Mirah] Installing extension: ${apkFile.name}")

        val jarBytes = try {
            DexConverter.convertApkToJar(apkFile)
        } catch (e: Exception) {
            throw RuntimeException("Failed to convert APK: ${apkFile.name}", e)
        }

        val jarFileName = apkFile.nameWithoutExtension + ".jar"
        val jarFile = File(extensionsDir, jarFileName)
        jarFile.writeBytes(jarBytes)

        println("[Mirah] Extension installed to: ${jarFile.absolutePath}")

        val loaded = ExtensionLoader.loadFromJar(
            jarFile = jarFile,
            packageName = apkFile.nameWithoutExtension,
            versionName = "installed"
        ) ?: throw RuntimeException("Extension installed but no sources found in: ${apkFile.name}")

        println("[Mirah] Loaded ${loaded.sources.size} source(s) from ${apkFile.name}")

        return loaded
    }

    fun loadAllInstalled(): List<LoadedExtension> {
        val dir = File(System.getProperty("user.home"), ".mirah/extensions")
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        return ExtensionLoader.loadAllFromDirectory(dir)
    }

    fun uninstall(packageName: String): Boolean {
        val jarFile = File(extensionsDir, "$packageName.jar")
        return if (jarFile.exists()) {
            jarFile.delete()
        } else {
            false
        }
    }

    fun getInstalledPackageNames(): List<String> {
        val dir = File(System.getProperty("user.home"), ".mirah/extensions")
        if (!dir.exists() || !dir.isDirectory) return emptyList()
        return dir.listFiles { file -> file.extension == "jar" }
            ?.map { it.nameWithoutExtension } ?: emptyList()
    }
}
