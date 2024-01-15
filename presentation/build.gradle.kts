import org.jetbrains.kotlin.kapt3.base.Kapt.kapt
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id(libs.plugins.hilt.plugin.get().pluginId)
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

    implementation(libs.appcompat)
    implementation(libs.material)

    // androidx
    implementation(libs.bundles.androidx)

    //compose
    implementation(libs.bundles.compose)
    implementation(platform(libs.compose.bom))

    // androidx-compose
    implementation(libs.bundles.androidx.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    testImplementation(libs.junit)

    // android test
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test.junit)
    androidTestImplementation(libs.bundles.android.test)

    // debug
    debugImplementation(libs.bundles.compose.debug)

    // naver map
    api(libs.bundles.naver.map)

    // multiple permission
    implementation("com.google.accompanist:accompanist-permissions:0.30.0")

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-beta01")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("com.google.android.material:material:1.4.0")
}