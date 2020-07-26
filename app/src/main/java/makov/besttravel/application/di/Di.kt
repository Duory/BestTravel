package makov.besttravel.application.di

import android.app.Application
import toothpick.Scope
import toothpick.Toothpick
import toothpick.configuration.Configuration

object Di {

    fun init() {
        Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
    }

    fun openRootScope(application: Application): Scope {
        return Toothpick.openScope(Scopes.ROOT).installModules(
            AppModule(application),
            RootApiModule()
        )
    }

    object Scopes {
        const val ROOT = "scope.ROOT"
        const val APP = "scope.APP"
    }
}