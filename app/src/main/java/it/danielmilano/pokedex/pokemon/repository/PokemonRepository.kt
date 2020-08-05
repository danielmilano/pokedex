package it.danielmilano.pokedex.pokemon.repository

import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.DataResponse
import it.danielmilano.pokedex.pokemon.model.Pokemon

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemonDetail(url: String): DataResponse<Pokemon> {
        val result = try {
            api.getPokemonDetail(url)
        } catch (e: Exception) {
            return DataResponse.Error(e)
        }

        return DataResponse.Success(data = result)
    }
}