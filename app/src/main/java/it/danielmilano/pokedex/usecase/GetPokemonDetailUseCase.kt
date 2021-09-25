package it.danielmilano.pokedex.usecase

import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.pokemon.repository.PokemonRepository

class GetPokemonDetailUseCase(
    private val repository: PokemonRepository,
    private val pokemonDao: PokemonDAO
) {
    suspend operator fun invoke(name: String, url: String): Pokemon {
        return pokemonDao.getByName(name) ?: repository.getPokemonDetail(url)
            .also { pokemonDao.add(it) }
    }
}