import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val naverMapClientId: String = properties.getProperty("naverMapClientId")

android {
    namespace = "com.example.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders["naverMapClientId"] = naverMapClientId
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
    implementation(project(":domain"))

    implementation(libs.androidx.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.runtime.ktx)
    implementation(libs.androidx.activity)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test.junit)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)

    // naver map to compose
    implementation("io.github.fornewid:naver-map-compose:1.3.3")
    // naver map SDK
    implementation("com.naver.maps:map-sdk:3.16.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("io.github.fornewid:naver-map-location:16.0.0")
}