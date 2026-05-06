package android.app

import android.content.Context

class Application : Context("mirah.app") {
    companion object {
        var instance: Application = Application()
    }
}