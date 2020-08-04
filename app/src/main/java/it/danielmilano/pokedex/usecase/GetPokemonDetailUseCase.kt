package it.danielmilano.pokedex.usecase

import it.danielmilano.pokedex.pokemon.repository.PokemonRepository

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {

    operator fun invoke(url: String) = repository.getPokemonDetail(url)
}