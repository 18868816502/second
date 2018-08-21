# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\C\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keep class com.beihui.market.ui.activity.DailyMissonActivity{
   public *;
}

-keepclassmembers class com.beihui.market.test.ui.activity.DailyMissonActivity$JsInterration{
   public *;
}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#BaseRecyclerViewHolderHelper
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers public class * extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(android.view.View);
}
# glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# banner
-keep class com.youth.banner.** {
    *;
 }
 -keep attributes *Annotation*

 -keep attributes *JavascriptInterface*

 -keep class com.just.agentweb.** {
     *;
 }
 -dontwarn com.just.agentweb.**

 -keepclasseswithmembers class com.beihui.market.ui.activity.DailyMissonActivity$JsInterration {
       <methods>;
       public *;
 }
