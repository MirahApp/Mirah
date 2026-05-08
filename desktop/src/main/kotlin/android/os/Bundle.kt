package android.os

open class Bundle {
    private val data: MutableMap<String, Any?> = mutableMapOf()

    fun putString(key: String, value: String?) { data[key] = value }
    fun getString(key: String, defaultValue: String? = null): String? = data[key] as? String ?: defaultValue
    fun putInt(key: String, value: Int) { data[key] = value }
    fun getInt(key: String, defaultValue: Int = 0): Int = data[key] as? Int ?: defaultValue
}
