package mirah.extensions

import java.io.File
import java.lang.reflect.InvocationTargetException

object ExtensionDiagnostics {
    private data class FailureInfo(
        var count: Int = 0,
        val samples: MutableSet<String> = linkedSetOf()
    )

    private val failures = linkedMapOf<String, FailureInfo>()
    private val lock = Any()

    fun recordFailure(jarName: String, className: String, throwable: Throwable) {
        val key = classify(throwable)
        synchronized(lock) {
            val entry = failures.getOrPut(key) { FailureInfo() }
            entry.count += 1
            if (entry.samples.size < 8) {
                entry.samples.add("$jarName::$className")
            }
            writeReportLocked()
        }
    }

    private fun classify(throwable: Throwable): String {
        val root = rootCause(throwable)
        return when (root) {
            is NoClassDefFoundError -> "NoClassDefFoundError:${root.message ?: "unknown"}"
            is ClassNotFoundException -> "ClassNotFoundException:${root.message ?: "unknown"}"
            is NoSuchMethodError -> "NoSuchMethodError:${root.message ?: "unknown"}"
            is NoSuchMethodException -> "NoSuchMethodException:${root.message ?: "unknown"}"
            is NoSuchFieldError -> "NoSuchFieldError:${root.message ?: "unknown"}"
            is NullPointerException -> "NullPointerException"
            else -> "${root.javaClass.simpleName}:${root.message ?: "unknown"}"
        }
    }

    private fun rootCause(throwable: Throwable): Throwable {
        var current = throwable
        if (current is InvocationTargetException && current.cause != null) {
            current = current.cause!!
        }
        while (current.cause != null && current.cause !== current) {
            current = current.cause!!
        }
        return current
    }

    private fun writeReportLocked() {
        val logsDir = File(System.getProperty("user.home"), ".mirah/logs")
        logsDir.mkdirs()
        val reportFile = File(logsDir, "extension-compat-report.txt")

        val report = buildString {
            appendLine("Mirah Extension Compatibility Report")
            appendLine("Generated: ${java.time.LocalDateTime.now()}")
            appendLine()
            appendLine("Top compatibility/runtime failures captured by extension loader.")
            appendLine("Patch stubs/interfaces for the highest-frequency keys first.")
            appendLine()
            failures.entries.sortedByDescending { it.value.count }.forEach { (key, info) ->
                appendLine("- $key")
                appendLine("  count=${info.count}")
                if (info.samples.isNotEmpty()) {
                    appendLine("  samples:")
                    info.samples.forEach { sample ->
                        appendLine("    - $sample")
                    }
                }
                appendLine()
            }
        }
        reportFile.writeText(report)
    }
}
