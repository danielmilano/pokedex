package it.danielmilano.pokedex.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.danielmilano.pokedex.pokemon.model.Pokemon

@Dao
interface PokemonDAO {

    @Query("SELECT * FROM pokemon WHERE id = :id")
    fun getById(id: String?): LiveData<Pokemon>

    @Query("SELECT * FROM pokemon WHERE name = :name")
    fun getByName(name: String?): LiveData<Pokemon>

    @Query("SELECT * FROM pokemon")
    fun all(): LiveData<List<Pokemon>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(pokemon: Pokemon)

    @Query("DELETE FROM pokemon")
    fun deleteAll()

    @Delete
    fun delete(model: Pokemon)
}
