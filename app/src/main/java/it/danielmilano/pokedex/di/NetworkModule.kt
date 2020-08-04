package it.danielmilano.pokedex.di

import it.danielmilano.pokedex.ApiConstant
import it.danielmilano.pokedex.BuildConfig
import it.danielmilano.pokedex.api.PokemonApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideHttpLoggingInterceptor() }
    single { provideHttpClient(get()) }
    single { provideConverterFactory() }
    single { providePokemonApi(ApiConstant.BASE_URL, get(), get()) }
}

private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return logging
}

private fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

private fun provideConverterFactory() = GsonConverterFactory.create()

private fun providePokemonApi(
    baseUrl: String,
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
): PokemonApi = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(converterFactory)
    .client(okHttpClient)
    .build()
    .create(PokemonApi::class.java)