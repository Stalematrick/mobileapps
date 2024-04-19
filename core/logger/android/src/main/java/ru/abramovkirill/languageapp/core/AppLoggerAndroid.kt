package ru.abramovkirill.languageapp.core

import android.util.Log

class AppLoggerAndroid : AppLogger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}
