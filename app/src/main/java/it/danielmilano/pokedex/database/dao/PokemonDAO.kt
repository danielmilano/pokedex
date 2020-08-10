package it.danielmilano.pokedex.database.dao

import androidx.room.*
import it.danielmilano.pokedex.pokemon.model.Pokemon

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getById(id: String?): Pokemon?

    @Query("SELECT * FROM pokemon WHERE name = :name")
    fun getByName(name: String?): Pokemon?

    @Query("SELECT * FROM pokemon")
    fun all(): List<Pokemon>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(pokemon: Pokemon)

    @Query("DELETE FROM pokemon")
    fun deleteAll()

    @Delete
    fun delete(model: Pokemon)
}
