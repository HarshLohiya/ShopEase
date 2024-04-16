plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.newEcom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.newEcom"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    packagingOptions {
        merge ("META-INF/NOTICE.md")
        merge ("META-INF/LICENSE.md")
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Load Image from Net
    implementation("com.squareup.picasso:picasso:2.8")
    //Round Image
    implementation("com.makeramen:roundedimageview:2.3.0")
    //Search Bar
    implementation("com.github.mancj:MaterialSearchBar:0.8.5")
    // Carousel
    implementation("me.relex:circleindicator:2.1.6")
    implementation("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")
    //API call
    implementation("com.android.volley:volley:1.2.1")
    //
//    implementation("io.github.tutorialsandroid:kalertdialog:20.4.8")
    implementation ("com.github.f0ris.sweetalert:library:1.6.2")
    // Send Email
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
    //Firebase Dynamic Link
    implementation ("com.google.firebase:firebase-dynamic-links:21.2.0")
    implementation ("com.google.firebase:firebase-core:21.1.1")
    //Shimmer Effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    //Lottie Animation
    implementation ("com.airbnb.android:lottie:6.3.0")
}