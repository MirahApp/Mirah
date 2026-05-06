package mirah.extensions

import com.googlecode.d2j.dex.Dex2jar
import java.io.File
import java.util.zip.ZipFile

object DexConverter {
    fun convertApkToJar(apkFile: File): ByteArray {
        var dexTempFile: File? = null
        var jarTempFile: File? = null

        try {
            // Extract classes.dex from APK
            val dexBytes = ZipFile(apkFile).use { zip ->
                val entry = zip.getEntry("classes.dex")
                    ?: throw IllegalArgumentException("No classes.dex found in APK: ${apkFile.name}")
                zip.getInputStream(entry).use { it.readBytes() }
            }

            // Write DEX to temp file
            dexTempFile = File.createTempFile("mirah_dex_", ".dex").apply {
                deleteOnExit()
                writeBytes(dexBytes)
            }

            // Output JAR temp file
            jarTempFile = File.createTempFile("mirah_jar_", ".jar").apply {
                deleteOnExit()
            }

            // Convert DEX to JAR using fluent Dex2jar API
            Dex2jar.from(dexTempFile)
                .skipDebug(false)
                .to(jarTempFile.toPath())

            return jarTempFile.readBytes()

        } finally {
            dexTempFile?.delete()
            jarTempFile?.delete()
        }
    }
}