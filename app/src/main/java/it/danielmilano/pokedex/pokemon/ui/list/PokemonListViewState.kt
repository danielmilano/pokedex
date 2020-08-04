package it.danielmilano.pokedex.pokemon.ui.list

import androidx.paging.PagedList
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

data class PokemonListViewState(
    val fetchStatus: FetchStatus,
    val pokemon: PagedList<PokemonListItem>
)

sealed class PokemonListViewEffect {
    data class ShowSnackbar(val message: String) : PokemonListViewEffect()
    data class ShowToast(val message: String) : PokemonListViewEffect()
}

sealed class PokemonListViewEvent {
    data class OnPokemonCliked(val pokemon: PokemonListItem) : PokemonListViewEvent()
    object FetchPokemonList : PokemonListViewEvent()
}

sealed class FetchStatus {
    object Fetching : FetchStatus()
    object Fetched : FetchStatus()
    object NotFetched : FetchStatus()
}