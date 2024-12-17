// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.10")
    }
}


plugins {
    id("com.android.application") version "8.1.4" apply false
    id ("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false


    id("com.google.dagger.hilt.android") version "2.46" apply false
    id ("com.google.gms.google-services") version "4.3.15" apply false
}
tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

