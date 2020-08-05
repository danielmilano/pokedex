package it.danielmilano.pokedex.api

import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.model.PaginatedWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface PokemonApi {

    @GET("{data_type}")
    fun getInitialDataList(@Path(value = "data_type") dataType: String): Call<PaginatedWrapper<PokemonListItem>>

    @GET
    fun getDataList(@Url url: String): Call<PaginatedWrapper<PokemonListItem>>

    @GET
    suspend fun getPokemonDetail(@Url url: String): Pokemon
}