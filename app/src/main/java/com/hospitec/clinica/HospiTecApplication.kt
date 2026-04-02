package com.hospitec.clinica

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Clase Application principal de HospiTec
 * Configuración global de la aplicación y logging profesional
 */
@HiltAndroidApp
class HospiTecApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Configurar logging profesional
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // En producción, usar un árbol de logging más apropiado
            Timber.plant(CrashReportingTree())
        }
        
        Timber.d("🚀 HospiTec Application iniciada")
    }
    
    /**
     * Árbol de logging para producción que reporta crashes
     */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority >= android.util.Log.WARN) {
                // Aquí se podría integrar con servicios de crash reporting como Crashlytics
                // FirebaseCrashlytics.getInstance().log("$tag: $message")
            }
        }
    }
}
