package android.content

import java.util.prefs.Preferences

interface SharedPreferences {
    fun getString(key: String, defValue: String?): String?
    fun getInt(key: String, defValue: Int): Int
    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun getFloat(key: String, defValue: Float): Float
    fun getLong(key: String, defValue: Long): Long
    fun getStringSet(key: String, defValues: Set<String>?): Set<String>?
    fun contains(key: String): Boolean
    fun getAll(): Map<String, *>
    fun edit(): Editor
    fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener)
    fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener)

    interface Editor {
        fun putString(key: String, value: String?): Editor
        fun putInt(key: String, value: Int): Editor
        fun putBoolean(key: String, value: Boolean): Editor
        fun putFloat(key: String, value: Float): Editor
        fun putLong(key: String, value: Long): Editor
        fun putStringSet(key: String, values: Set<String>?): Editor
        fun remove(key: String): Editor
        fun clear(): Editor
        fun commit(): Boolean
        fun apply()
    }

    interface OnSharedPreferenceChangeListener {
        fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String)
    }
}

class JavaSharedPreferences(nodeName: String) : SharedPreferences {
    private val prefs: Preferences = Preferences.userRoot().node("mirah/extensions/prefs/$nodeName")
    private val listeners = mutableListOf<SharedPreferences.OnSharedPreferenceChangeListener>()

    override fun getString(key: String, defValue: String?): String? = prefs.get(key, defValue)
    override fun getInt(key: String, defValue: Int): Int = prefs.getInt(key, defValue)
    override fun getBoolean(key: String, defValue: Boolean): Boolean = prefs.getBoolean(key, defValue)
    override fun getFloat(key: String, defValue: Float): Float = prefs.getFloat(key, defValue)
    override fun getLong(key: String, defValue: Long): Long = prefs.getLong(key, defValue)

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        val stored = prefs.get(key, null) ?: return defValues
        return if (stored.isEmpty()) emptySet() else stored.split(",").toSet()
    }

    override fun contains(key: String): Boolean = prefs.get(key, null) != null

    override fun getAll(): Map<String, *> {
        val map = mutableMapOf<String, Any?>()
        for (key in prefs.keys()) {
            map[key] = prefs.get(key, null)
        }
        return map
    }

    override fun edit(): SharedPreferences.Editor = JavaSharedPreferencesEditor(prefs)

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        listeners.add(listener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        listeners.remove(listener)
    }
}

class JavaSharedPreferencesEditor(private val prefs: Preferences) : SharedPreferences.Editor {
    override fun putString(key: String, value: String?): SharedPreferences.Editor {
        if (value == null) prefs.remove(key) else prefs.put(key, value)
        return this
    }

    override fun putInt(key: String, value: Int): SharedPreferences.Editor {
        prefs.putInt(key, value)
        return this
    }

    override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
        prefs.putBoolean(key, value)
        return this
    }

    override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
        prefs.putFloat(key, value)
        return this
    }

    override fun putLong(key: String, value: Long): SharedPreferences.Editor {
        prefs.putLong(key, value)
        return this
    }

    override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor {
        val joined = values?.joinToString(",") ?: ""
        prefs.put(key, joined)
        return this
    }

    override fun remove(key: String): SharedPreferences.Editor {
        prefs.remove(key)
        return this
    }

    override fun clear(): SharedPreferences.Editor {
        prefs.clear()
        return this
    }

    override fun commit(): Boolean {
        prefs.flush()
        return true
    }

    override fun apply() {
        prefs.flush()
    }
}
