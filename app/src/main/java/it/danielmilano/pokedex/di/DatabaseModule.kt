package it.danielmilano.pokedex.di

import androidx.room.Room
import it.danielmilano.pokedex.R
import it.danielmilano.pokedex.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            androidApplication().baseContext.getString(R.string.app_name)
        ).build()
    }

    single {
        get<AppDatabase>().pokemonDAO()
    }
    single {
        get<AppDatabase>().pokemonItemListDAO()
    }
}
