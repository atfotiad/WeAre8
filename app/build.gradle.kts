plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.atfotiad.weare8"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.atfotiad.weare8"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "BASE_URL", "\"https://api-uat.test.aws.the8app.com/\"")
        buildConfigField("String", "JWT_TOKEN", "\"eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImN0eSI6IkpXVCJ9.eyJzdWIiOiJlMjhiYjNkNy1lO" +
                "DBjLTQ0YjgtYjg3My0xZWRmMzkwNDAwZTIiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZ" +
                "nQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOiJJbmZsdWVuY2VyIiw" +
                "iYXV0aF90aW1lIjoiMTcyNjc0ODY2MiIsImF1ZCI6WyJ1c2Vycy5hcGkiLCJwYXltZW50cy5h" +
                "cGkiLCJzb2NpYWwuYXBpIiwiZ3JhcGhzLmFwaSIsImNhbXBhaWduZGVsaXZlcnkuYXBpIi" +
                "wiY29udGVudC5hcGkiLCJmbG93cy5hcGkiLCJtb2RlcmF0aW9uLmFwaSJdLCJjbGllbnRfa" +
                "WQiOiJjaXRpemVucy5jbGllbnQiLCJleHAiOjE3MjkzNDA2NjIsImlzcyI6Imh0dHBzOi8vYXBp" +
                "LXVhdC50ZXN0LmF3cy50aGU4YXBwLmNvbS9laWdodGF1dGhhcGkifQ.KeVbjTCWhGXty-" +
                "0svwkpNG-UQnnnJ4NIX1mpIQKAF-OguKxIYakYCcazLDz2vbZjG61S__8uMYYBZR-" +
                "67t8Njhayh71zM8GfM1L5hV0w-nYUPrV7IpYpaFX1gCFD_-3l2nz--" +
                "CCFmKgRirqnyR5d_S8xtZYOffB6TRB2Rzwkk-9ilUD585T0J0zKx1-" +
                "qoRlj3ki3stsFdMnb8zmovjCLQasC9RbV4Y_7wTBrPqoQh17U7HtGpYdSVKWJiShTLBhTL4" +
                "tr8fBc5B9qEIA4O0FsLggetWiPr_sB6zhfJ4XmGEqgzqlji3CHzH5Pe7lSrOl3KrrfCe9QKsDy-" +
                "tyTz2xi0w\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui)
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.media3.exoplayer.hls)

    implementation(libs.coil.compose)
    kapt(libs.hilt.android.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}