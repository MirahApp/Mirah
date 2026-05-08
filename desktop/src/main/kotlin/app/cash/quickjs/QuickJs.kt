package app.cash.quickjs

class QuickJs : AutoCloseable {
    companion object {
        @JvmStatic
        fun create(): QuickJs = QuickJs()
    }
    fun <T> get(name: String, type: Class<T>): T? = null
    fun set(name: String, type: Class<*>, instance: Any) {}
    fun evaluate(script: String): Any? = null
    override fun close() {}
}