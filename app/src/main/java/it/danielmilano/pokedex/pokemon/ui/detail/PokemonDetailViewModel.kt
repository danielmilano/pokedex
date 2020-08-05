package it.danielmilano.pokedex.pokemon.ui.detail

import android.app.Application
import androidx.lifecycle.viewModelScope
import it.danielmilano.pokedex.base.BaseViewModel
import it.danielmilano.pokedex.base.DataResponse
import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class PokemonDetailViewModel(
    application: Application,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val pokemonDAO: PokemonDAO
) : BaseViewModel<PokemonDetailViewState, PokemonDetailViewEffect, PokemonDetailViewEvent>(
    application
) {

    init {
        viewState = PokemonDetailViewState(fetchStatus = FetchStatus.NotFetched, pokemon = null)
    }

    override fun process(viewEvent: PokemonDetailViewEvent) {
        super.process(viewEvent)
        when (viewEvent) {
            is PokemonDetailViewEvent.OnSharePokemon -> {
            }
            is PokemonDetailViewEvent.FetchPokemonDetail -> {
                fetchPokemonDetail(viewEvent.url)
            }
        }
    }

    private fun fetchPokemonDetail(url: String) {
        viewState = viewState.copy(fetchStatus = FetchStatus.Fetching)
        viewModelScope.launch {
            when (val result = getPokemonDetailUseCase(url)) {
                is DataResponse.Error -> {
                    viewState = viewState.copy(fetchStatus = FetchStatus.Fetched)
                    viewEffect = PokemonDetailViewEffect.ShowToast(message = result.message)
                }
                is DataResponse.Success -> {
                    thread {
                        pokemonDAO.add(result.data)
                    }
                    viewState =
                        viewState.copy(fetchStatus = FetchStatus.Fetched, pokemon = result.data)
                }
            }
        }
    }
}