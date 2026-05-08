package mirah.extensions

import android.app.Application
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory
import eu.kanade.tachiyomi.network.NetworkHelper

object InjektSetup {
    fun init() {
        Injekt.addSingleton(Application.instance)
        Injekt.addSingletonFactory { NetworkHelper() }
    }
}