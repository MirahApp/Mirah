package android.content

open class Context(val packageName: String) {
    fun getSharedPreferences(name: String, mode: Int): SharedPreferences {
        return JavaSharedPreferences(name)
    }

    fun getSystemService(name: String): Any? = null

    companion object {
        const val MODE_PRIVATE = 0
    }
} 