# Reglas ProGuard para HospiTec - Aplicación Profesional de Gestión de Equipos Médicos

# Reglas generales de Android
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# Mantener clases de Compose
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# Mantener ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# Mantener clases de Room
-keep class * extends androidx.room.RoomDatabase {
    public static <T> T getDatabase(android.content.Context);
}
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Mantener clases de Hilt
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# Mantener clases de Supabase
-keep class io.github.jan-tennert.supabase.** { *; }
-dontwarn io.github.jan-tennert.supabase.**

# Mantener clases de Retrofit
-keepattributes Signature
-keepattributes Exceptions
-keep class retrofit2.** { *; }
-keepattributes *Annotation*
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Mantener clases de OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Mantener clases de Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Mantener clases de Serialización
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keep,includedescriptorclasses class com.hospitec.clinica.**$$serializer { *; }
-keepclassmembers class com.hospitec.clinica.** {
    *** Companion;
}
-keepclasseswithmembers class com.hospitec.clinica.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Mantener clases de la aplicación
-keep class com.hospitec.clinica.** { *; }
-keep class com.hospitec.clinica.data.** { *; }
-keep class com.hospitec.clinica.domain.** { *; }
-keep class com.hospitec.clinica.ui.** { *; }

# Optimizaciones específicas
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

# Mantener métodos nativos
-keepclasseswithmembernames class * {
    native <methods>;
}

# Mantener métodos de callback
-keepclassmembers class * {
    void on*(android.view.View);
}

# Mantener clases de Activity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

# Mantener View constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Mantener Parcelable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Mantener Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Mantener R classes
-keep class **.R$* {
    public static <fields>;
}

# Mantener clases de Material Design
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**

# Mantener clases de WorkManager
-keep class androidx.work.impl.** { *; }
-dontwarn androidx.work.impl.**

# Mantener clases de DataStore
-keep class androidx.datastore.** { *; }
-dontwarn androidx.datastore.**

# Mantener clases de Timber
-keep class timber.log.** { *; }
-dontwarn timber.log.**

# Mantener clases de Coil
-keep class coil.** { *; }
-dontwarn coil.**

# Reglas para reducir tamaño
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# Mantener información de debugging en debug builds
-dontshrink
-dontoptimize
-dontpreverify