package android.app

import android.content.Context
import android.content.ContextWrapper

open class Application : ContextWrapper(Context("mirah.app")) {
    companion object {
        var instance: Application = Application()
    }
}