plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id(libs.plugins.hilt.plugin.get().pluginId)
}

android {
    namespace = "com.example.android_kcs"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.android_kcs"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    // androidx
    implementation(libs.bundles.androidx)

    // compose
    implementation(libs.bundles.compose)

    // test
    testImplementation(libs.junit)

    // android test
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test.junit)

    // debug
    debugImplementation(libs.bundles.compose.debug)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Timber
    implementation(libs.timber)
}