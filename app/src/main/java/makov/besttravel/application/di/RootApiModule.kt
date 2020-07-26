package makov.besttravel.application.di

import retrofit2.Retrofit
import toothpick.config.Module

class RootApiModule : Module() {

    init {
        bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).providesSingleton()
    }
}