// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    kotlin("android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.0" apply false
}