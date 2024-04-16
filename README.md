
# ShopEase

ShopEase App is a dynamic e-commerce Android application crafted with Java, featuring an intuitive in-app admin panel for efficient management and hassle-free user authentication via email or Google account integration. Leveraging Firebase Firestore as the backend database, the app boasts a robust cart system and personalized profile pages for users to track wishlisted and ordered products. 


## Features

- **Short Link Sharing**: Enables effortless product sharing with automated email generation containing order details upon purchase.
- **Sophisticated Search**: Engineered search functionality allows users to easily find products based on tags, enhancing user experience.
- **Similar Products Section**: The product page includes a section showcasing similar products, aiding users in discovering relevant items.
- **Rating and Review System**: Users can rate and review products directly from their orders page, facilitating informed purchasing decisions for others.
- **Automated Email Generation**: Automatically generates emails containing order details upon purchase, enhancing communication and providing customers with comprehensive order information.

## Demo

Link of the apk file:
https://drive.google.com/file/d/1FmDr8Ra8YXDWiJwh8suTgCcDI2qKtyOb/view?usp=sharing
## Tech Stack

**IDE:** Android Studio

**Languages:** Java, XML

**Database:** Firebase Firestore for data, and Firebase Storage for Media

### Libraries
    //Load image from URL
    implementation("com.squareup.picasso:picasso:2.8")
    
    //Search bar
    implementation("com.github.mancj:MaterialSearchBar:0.8.5")

    //Rounded image
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("me.relex:circleindicator:2.1.6")

    //Carousel
    implementation("org.imaginativeworld.whynotimagecarousel:whynotimagecarousel:2.1.0")

    //Alert dialog
    implementation ("com.github.f0ris.sweetalert:library:1.6.2")

    //Send email
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")

    //Firebase Dynamic Link
    implementation ("com.google.firebase:firebase-dynamic-links:21.2.0")
    implementation ("com.google.firebase:firebase-core:21.1.1")

    //Shimmer effect
    implementation ("com.facebook.shimmer:shimmer:0.5.0")

    //Lottie animation
    implementation ("com.airbnb.android:lottie:6.3.0")
