plugins {
    kotlin("multiplatform") version "2.0.20"
}

group = "com.yanpegyn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinxHtmlJsVersion = "0.11.0"
val kotlinxDatetimeVersion = "0.6.1"

dependencies {
    commonMainImplementation(kotlin("stdlib-js"))
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-html:$kotlinxHtmlJsVersion") //https://github.com/Kotlin/kotlinx.html
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinxHtmlJsVersion") //https://github.com/Kotlin/kotlinx.html
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion") //https://github.com/Kotlin/kotlinx-datetime
    commonMainImplementation(npm("@popperjs/core", "2.11.8")) // https://www.npmjs.com/package/@popperjs/core
    commonMainImplementation(npm("bootstrap", "5.3.3")) // https://www.npmjs.com/package/bootstrap }
    // ...
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test")) // Brings all the platform dependencies automatically
        }
    }
}