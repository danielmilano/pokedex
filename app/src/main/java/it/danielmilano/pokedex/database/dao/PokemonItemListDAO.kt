package it.danielmilano.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

@Dao
interface PokemonItemListDAO {

    @Query("SELECT * FROM pokemonlistitem WHERE name = :name")
    fun getById(name: String?): LiveData<PokemonListItem>

    @Query("SELECT * FROM pokemonlistitem WHERE name = :url")
    fun getByUrl(url: String?): LiveData<PokemonListItem>

    @Query("SELECT * FROM pokemonlistitem")
    fun all(): DataSource.Factory<Int, PokemonListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(pokemon: List<PokemonListItem>)

    @Query("DELETE FROM pokemonlistitem")
    fun deleteAll()

    @Delete
    fun delete(model: PokemonListItem)
}
