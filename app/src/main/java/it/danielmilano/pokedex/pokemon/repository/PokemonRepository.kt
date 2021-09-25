package it.danielmilano.pokedex.pokemon.repository

import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.pokemon.model.Pokemon

class PokemonRepository(private val api: PokemonApi) {

    suspend fun getPokemonDetail(url: String): Pokemon {
        return api.getPokemonDetail(url)
    }
}