package it.danielmilano.pokedex.pokemon.ui.list

import androidx.lifecycle.*
import androidx.paging.PagedList
import it.danielmilano.pokedex.base.NetworkState
import it.danielmilano.pokedex.base.PagedListResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.usecase.GetPagedListUseCase

class PokemonListViewModel(private val getListUseCase: GetPagedListUseCase) : ViewModel() {

    private val pagedListResult: MutableLiveData<PagedListResult<PokemonListItem>> = MutableLiveData()

    val pokemonList: LiveData<PagedList<PokemonListItem>> = pagedListResult.switchMap { it.result }

    val networkState: LiveData<NetworkState> = pagedListResult.switchMap { it.networkState }

    val error : LiveData<String?> = pagedListResult.switchMap { it.error }

    val lastPage: LiveData<Boolean> = pagedListResult.switchMap { it.lastPage }

    init {
        getPokemonList()
    }

    private fun getPokemonList() {
        pagedListResult.value = getListUseCase()
    }

    fun retry() {
        pagedListResult.value?.retry?.invoke()
    }
}