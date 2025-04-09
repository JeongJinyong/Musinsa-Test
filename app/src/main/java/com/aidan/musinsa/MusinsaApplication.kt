package com.aidan.musinsa

import android.app.Application
import com.aidan.musinsa.di.networkModule
import com.airbnb.mvrx.Mavericks
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application 클래스
 */
class MusinsaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Mavericks 초기화
        Mavericks.initialize(this)
        
        // Koin 초기화
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MusinsaApplication)
            modules(listOf(networkModule))
        }
    }
}