package ru.abramovkirill.languageapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import org.koin.core.context.startKoin
import ru.abramovkirill.languageapp.di.appModules

class LanguageApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        _instance = this

        startKoin {
            modules(appModules)
        }
    }

    override fun newImageLoader() =
        ImageLoader.Builder(this)
            .crossfade(true)
            .build()

    companion object {
        private var _instance: LanguageApplication? = null
        val instance get() = requireNotNull(_instance)
    }
}
