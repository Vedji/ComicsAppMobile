plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.comicsappmobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.comicsappmobile"
        minSdk = 31
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_BASE_URL", "\"http://85.193.81.103:5000\"")
        buildConfigField("Boolean", "ENABLE_LOGS", "true")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "ENABLE_LOGS", "false")
        }
        debug {
            // buildConfigField("String", "API_KEY", "\"debug-key\"")
            buildConfigField("Boolean", "ENABLE_LOGS", "true")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Koin Core for Dependency Injection
    implementation(libs.koin.android)
    // Koin Compose integration
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.drawerlayout)

    implementation(libs.androidx.foundation)


    // Retrofit
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.kotlin.coroutines.adapter)    // Для работы с Coroutines (если нужно)

    // Json
    // Gson
    // Конвертер JSON (если вы используете GSON для обработки JSON)
    implementation(libs.gson)
    implementation(libs.converter.gson)
    // Moshi
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)

    // OkHttp (для логирования запросов)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Основная зависимость Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    // Зависимость для работы с GIF (опционально)
    implementation(libs.coil.gif)
    // Зависимость для работы с SVG (опционально)
    implementation(libs.coil.svg)
    // implementation(libs.identity.doctypes.jvm)
    // implementation(libs.androidx.material3.jvmstubs)

    implementation(libs.accompanist.swiperefresh)


    // Add at 13.01.2025 7:02   Pager
    implementation(libs.accompanist.pager)
    // Add at 13.01.2025 7:02   Pager
    implementation(libs.accompanist.pager.indicators)
    // Add at 8:16 13.01.2025   ZoomImage
    implementation(libs.zoomable)
    // Add at 1:06 16.01.2025   DataStore
    implementation(libs.androidx.datastore.preferences)
    // Add at 16.01.2025 18:14  DragNDrop
    implementation(libs.reorderable)



    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}