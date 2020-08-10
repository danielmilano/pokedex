package it.danielmilano.pokedex.database.dao

import androidx.paging.DataSource
import androidx.room.*
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

@Dao
interface PokemonItemListDAO {

    @Query("SELECT * FROM pokemonlistitem WHERE name = :name")
    fun getById(name: String?): PokemonListItem

    @Query("SELECT * FROM pokemonlistitem WHERE name = :url")
    fun getByUrl(url: String?): PokemonListItem

    @Query("SELECT * FROM pokemonlistitem")
    fun all(): DataSource.Factory<Int, PokemonListItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(pokemon: List<PokemonListItem>)

    @Query("DELETE FROM pokemonlistitem")
    fun deleteAll()

    @Delete
    fun delete(pokemonListItem: PokemonListItem)
}
