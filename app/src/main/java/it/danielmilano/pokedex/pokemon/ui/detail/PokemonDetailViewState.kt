package it.danielmilano.pokedex.pokemon.ui.detail

import it.danielmilano.pokedex.pokemon.model.Pokemon

data class PokemonDetailViewState(val fetchStatus: FetchStatus, val pokemon: Pokemon?)

sealed class PokemonDetailViewEffect {
    data class ShowSnackbar(val message: String) : PokemonDetailViewEffect()
    data class ShowToast(val message: String) : PokemonDetailViewEffect()
}

sealed class PokemonDetailViewEvent {
    data class OnSharePokemon(val pokemon: Pokemon) : PokemonDetailViewEvent()
    data class FetchPokemonDetail(val url: String) : PokemonDetailViewEvent()
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}