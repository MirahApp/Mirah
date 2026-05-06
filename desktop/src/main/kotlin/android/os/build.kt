package android.os

class Build {
    object VERSION {
        @JvmField val SDK_INT: Int = mirah.compat.android.AndroidBuild.VERSION_SDK_INT
        @JvmField val RELEASE: String = mirah.compat.android.AndroidBuild.VERSION_RELEASE
    }
    companion object {
        @JvmField val MANUFACTURER: String = mirah.compat.android.AndroidBuild.MANUFACTURER
        @JvmField val MODEL: String = mirah.compat.android.AndroidBuild.MODEL
        @JvmField val BRAND: String = mirah.compat.android.AndroidBuild.BRAND
    }
}