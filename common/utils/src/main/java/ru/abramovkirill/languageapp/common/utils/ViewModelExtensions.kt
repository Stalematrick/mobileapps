package ru.abramovkirill.languageapp.common.utils

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.initializeViewModel(
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    ViewModelProvider(
        viewModelStore,
        factoryPromise(),
        extrasProducer?.invoke() ?: this.defaultViewModelCreationExtras,
    )[VM::class.java]
}
