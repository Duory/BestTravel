package makov.besttravel.application.di

import android.app.Application
import toothpick.config.Module

class AppModule(application: Application) : Module() {

    init {
        bind(Application::class.java).toInstance(application)
    }
}