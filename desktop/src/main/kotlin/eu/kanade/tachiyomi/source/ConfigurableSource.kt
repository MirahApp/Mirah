package eu.kanade.tachiyomi.source

import android.content.SharedPreferences
import androidx.preference.PreferenceScreen

interface ConfigurableSource : Source {
    fun setupPreferenceScreen(screen: PreferenceScreen)
}