package it.danielmilano.pokedex

import android.app.Application
import it.danielmilano.pokedex.di.databaseModule
import it.danielmilano.pokedex.di.networkModule
import it.danielmilano.pokedex.di.pokemonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

val modules = listOf(networkModule, databaseModule, pokemonModule)

class PokedexApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PokedexApplication)
            modules(modules)
        }
    }
}