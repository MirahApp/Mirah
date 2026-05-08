package androidx.preference

open class Preference(val key: String = "") {
    var title: CharSequence = ""
    var summary: CharSequence = ""
    var isVisible: Boolean = true
    var isEnabled: Boolean = true
    var defaultValue: Any? = null
    private var changeListener: OnPreferenceChangeListener? = null
    private var clickListener: OnPreferenceClickListener? = null

    fun setOnPreferenceChangeListener(listener: OnPreferenceChangeListener?) {
        changeListener = listener
    }
    fun getOnPreferenceChangeListener(): OnPreferenceChangeListener? = changeListener
    fun setOnPreferenceClickListener(listener: OnPreferenceClickListener?) {
        clickListener = listener
    }

    fun interface OnPreferenceChangeListener {
        fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean
    }

    fun interface OnPreferenceClickListener {
        fun onPreferenceClick(preference: Preference): Boolean
    }
}

open class PreferenceScreen : Preference() {
    private val preferences = mutableListOf<Preference>()
    fun addPreference(preference: Preference) { preferences.add(preference) }
    fun getPreferenceCount(): Int = preferences.size
    fun getPreference(index: Int): Preference = preferences[index]
}

open class PreferenceGroup(key: String = "") : Preference(key) {
    private val preferences = mutableListOf<Preference>()
    fun addPreference(preference: Preference) { preferences.add(preference) }
}

class PreferenceCategory(key: String = "") : PreferenceGroup(key)

class SwitchPreferenceCompat(key: String = "") : Preference(key) {
    var isChecked: Boolean = false
}

class CheckBoxPreference(key: String = "") : Preference(key) {
    var isChecked: Boolean = false
}

class ListPreference(key: String = "") : Preference(key) {
    var entries: Array<CharSequence> = emptyArray()
    var entryValues: Array<CharSequence> = emptyArray()
    var value: String = ""
    fun findIndexOfValue(value: String): Int = entryValues.indexOfFirst { it == value }
}

class MultiSelectListPreference(key: String = "") : Preference(key) {
    var entries: Array<CharSequence> = emptyArray()
    var entryValues: Array<CharSequence> = emptyArray()
    var values: Set<String> = emptySet()
}

class EditTextPreference(key: String = "") : Preference(key) {
    var text: String = ""
}

class PreferenceManager {
    companion object {
        @JvmStatic
        fun getDefaultSharedPreferences(context: android.content.Context): android.content.SharedPreferences {
            return context.getSharedPreferences("default", android.content.Context.MODE_PRIVATE)
        }
    }
}