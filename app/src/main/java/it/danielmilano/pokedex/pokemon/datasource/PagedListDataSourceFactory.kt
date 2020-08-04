package it.danielmilano.pokedex.pokemon.datasource

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

class PagedListDataSourceFactory(
    dataType: String,
    api: PokemonApi
) : DataSource.Factory<String, PokemonListItem>() {

    private var pagedListDataSource = PagedListDataSource(dataType, api)

    val isLoading: LiveData<Boolean> = pagedListDataSource.isLoading

    val isInitialLoading: LiveData<Boolean> = pagedListDataSource.isInitialLoading

    val networkError: LiveData<String?> = pagedListDataSource.networkError

    override fun create(): DataSource<String, PokemonListItem> = pagedListDataSource

}