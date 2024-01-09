plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.mohanjp.runningtracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mohanjp.runningtracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        /*val googleMapApiKey = gradleLocalProperties(rootDir).getProperty("GOOGLE_MAP_API_KEY")
        manifestPlaceholders["GOOGLE_MAP_API_KEY"] = googleMapApiKey*/
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /**
     * intuit
     */
    implementation(libs.sdp.android)

    /**
     * navigation
     */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    /**
     * room
     */
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    /**
     * dagger - hilt
     */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    //ksp("androidx.hilt:hilt-compiler:1.1.0")

    /**
     * MP chart
     */
    implementation(libs.mp.android.chart)

    /**
     * google maps and location services
     */
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)

    /**
     * timber
     */
    implementation(libs.timber)

    /**
     * lifecycle - service
     */
    implementation(libs.androidx.lifecycle.service)
}