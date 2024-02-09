import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id(libs.plugins.hilt.plugin.get().pluginId)
    kotlin("plugin.serialization") version "1.7.20"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.android_kcs"
    compileSdk = 34

    defaultConfig {
        applicationId = "nainga_store.android_kcs"
        minSdk = 26
        targetSdk = 34
        versionCode = 4
        versionName = "3.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["dev.base.url"].toString())
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEV"
            manifestPlaceholders["appName"] = "@string/app_name_dev"
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }["prod.base.url"].toString())
            manifestPlaceholders["appName"] = "@string/app_name"
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
    implementation(libs.androidx.lifecycle.runtime.compose)

    // test
    testImplementation(libs.junit)

    // android test
    androidTestImplementation(libs.bundles.android.test)
    androidTestImplementation(libs.compose.test.junit)

    // debug
    debugImplementation(libs.bundles.compose.debug)

    // Timber
    implementation(libs.timber)

    // multiple permission
    implementation(libs.accompanist.permissions)

    // retrofit
    implementation(libs.bundles.retrofit)
    implementation(libs.bundles.squareup)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Mosh
    implementation(libs.squareupRetrofit2)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.1"))
    implementation("com.google.firebase:firebase-analytics-ktx:21.5.0")
    implementation("com.google.firebase:firebase-crashlytics-ktx:18.6.1")

}