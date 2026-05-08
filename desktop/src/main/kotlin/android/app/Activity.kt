package android.app

import android.content.Context
import android.content.Intent
import android.os.Bundle

open class Activity(val context: Context? = null) {
    open fun onCreate(savedInstanceState: Bundle?) {}
    open fun onDestroy() {}
    open fun startActivity(intent: Intent) {}
    open fun finish() {}
}
