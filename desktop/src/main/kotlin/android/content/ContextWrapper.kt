package android.content

open class ContextWrapper(base: Context?) : Context(base?.packageName ?: "mirah.app") {
    private var baseContext: Context? = base
    fun getBaseContext(): Context? = baseContext
}