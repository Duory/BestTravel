package makov.besttravel.application.di

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Provider

class RetrofitProvider: Provider<Retrofit> {
    override fun get(): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient.Builder().build())
            .baseUrl("https://yasen.hotellook.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}