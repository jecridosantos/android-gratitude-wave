plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile =
                file("E:\\Workspace\\GratitudeWave\\android\\gratitude-wave\\gratitude.keystore")
            storePassword = "gratitude"
            keyPassword = "gratitude"
            keyAlias = "key0"
        }
    }
    namespace = "com.jdosantos.gratitudewavev1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jdosantos.gratitudewavev1"
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("androidx.paging:paging-common-android:3.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
   // implementation("androidx.compose.foundation:foundation-desktop:1.6.8")
    //navigation
    val navVersion = "2.7.6"
    //noinspection GradleDependency
    implementation("androidx.navigation:navigation-compose:$navVersion")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    //dagger Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    //Paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation("androidx.paging:paging-compose:3.3.0")
    //Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    //Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")
    //Pager
    implementation("com.google.accompanist:accompanist-pager:0.15.0")
    //Notificaciones
    implementation("androidx.work:work-runtime:2.9.0")

    //gemini
    implementation("com.google.firebase:firebase-vertexai:16.0.0-beta01")
    implementation("com.google.guava:guava:32.0.1-jre")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.3-alpha")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))

   // implementation("androidx.compose.runtime:runtime:1.4.3")
    implementation("androidx.compose.ui:ui:1.6.7")

    // implementation("com.google.crypto.tink:tink-android:1.6.1")

    implementation("androidx.security:security-crypto:1.1.0-alpha03")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}