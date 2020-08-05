package it.danielmilano.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

@Database(entities = [Pokemon::class, PokemonListItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pokemonDAO(): PokemonDAO

    abstract fun pokemonItemListDAO(): PokemonItemListDAO
}
