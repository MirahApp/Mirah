plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)

    // Extension system
    implementation("net.dongliu:apk-parser:2.6.10")
    implementation("de.femtopedia.dex2jar:dex-translator:2.4.20")
    implementation("org.ow2.asm:asm:9.6")
    implementation("org.ow2.asm:asm-commons:9.6")

    // Locked versions matching what Mihon extensions compile against
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.14")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("io.reactivex:rxjava:1.3.8")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("uy.kohesive.injekt:injekt-core:1.16.1")
}

compose.desktop {
    application {
        mainClass = "mirah.MainKt"
    }
}