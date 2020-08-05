package it.danielmilano.pokedex.pokemon.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.PagedListResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.usecase.GetPagedListUseCase

class PokemonListViewModel(
    private val getListUseCase: GetPagedListUseCase
) : ViewModel() {

    private val pagedListResult: MutableLiveData<PagedListResult<PokemonListItem>> =
        MutableLiveData()

    val pokemonList: LiveData<PagedList<PokemonListItem>> =
        Transformations.switchMap(pagedListResult) {
            it.result
        }

    val isInitialLoading: LiveData<Boolean> =
        Transformations.switchMap(pagedListResult) {
            it.isInitialLoading
        }

    val networkError: LiveData<String?> =
        Transformations.switchMap(pagedListResult) {
            it.networkError
        }

    val isLoading: LiveData<Boolean> =
        Transformations.switchMap(pagedListResult) {
            it.isLoading
        }

    init {
        getPokemonList()
    }

    private fun getPokemonList() {
        pagedListResult.value = getListUseCase("pokemon")
    }
}