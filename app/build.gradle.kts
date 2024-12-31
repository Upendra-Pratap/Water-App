plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.waterapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.waterapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "IS_DEBUG", "false")
            buildConfigField("String", "API_KEY", "AndroidJUnit4AndroidJUnit4")
        }
        android {
            buildTypes {
                getByName("debug") {
                    isDebuggable = true
                    isMinifyEnabled = false
                    isShrinkResources = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                    buildConfigField("boolean", "IS_DEBUG", "true")
                    buildConfigField("String", "API_KEY", "\"http://192.168.1.2:3300/api/\"")
                    buildConfigField("String", "IMAGE_KEY", "\"http://192.168.1.2:3300/\"")
                }
            }
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
        viewBinding =true
        dataBinding =true
        buildConfig =true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("me.relex:circleindicator:2.1.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    implementation("androidx.security:security-crypto:1.1.0-alpha04")

    implementation("androidx.fragment:fragment-ktx:1.6.2")

    /* Alert dependency*/
    implementation("io.github.tutorialsandroid:kalertdialog:20.3.6")
    implementation("com.github.TutorialsAndroid:progressx:6.0.19")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")

    //ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    //Image compress
    implementation("id.zelory:compressor:3.0.1")

    //firebase
    implementation("com.google.firebase:firebase-messaging:23.4.1")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation ("com.google.firebase:firebase-auth:21.0.3")
    implementation ("com.google.firebase:firebase-database:20.0.4")
    implementation ("com.google.firebase:firebase-storage:20.0.1")


    //user permission
    implementation("com.karumi:dexter:6.2.2")


    // spinner
    implementation("com.github.skydoves:powerspinner:1.2.4")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.4")

    //Rx
    implementation("io.reactivex.rxjava2:rxjava:2.2.10")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")


    //Dagger Hilt
    /*    implementation 'com.google.dagger:hilt-android:2.51'
       // classpath "com.google.dagger:hilt-android-gradle-plugin:2.45"
        kapt 'com.google.dagger:hilt-compiler:2.46'*//*
    implementation 'com.google.dagger:hilt-android:2.39.1'
    kapt 'com.google.dagger:hilt-compiler:2.39.1'*/
    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-compiler:2.46")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    /* Alert dependency*/
    implementation("io.github.tutorialsandroid:kalertdialog:20.3.6")
    implementation("com.github.TutorialsAndroid:progressx:6.0.19")

    //Image compress
    implementation("id.zelory:compressor:3.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")

    //  implementation 'com.google.code.gson:gson:2.10'
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")



    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")

    implementation("com.google.firebase:firebase-bom:31.5.0")
    implementation("com.google.android.gms:play-services-auth:21.1.0")
    implementation("com.google.android.gms:play-services-wallet:19.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }
}