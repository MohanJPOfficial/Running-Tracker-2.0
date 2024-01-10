plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
//    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.mapsplatform.gradle)
}

android {
    namespace = "com.mohanjp.runningtracker"

    compileSdk = libs.versions.compilesdk.get().toInt()

    defaultConfig {
        applicationId = "com.mohanjp.runningtracker"
        minSdk = libs.versions.minsdk.get().toInt()
        targetSdk = libs.versions.targetsdk.get().toInt()
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
    implementation(libs.bundles.androidx.navigation.bundle)

    /**
     * room
     */
    implementation(libs.bundles.androidx.room.bundle)
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