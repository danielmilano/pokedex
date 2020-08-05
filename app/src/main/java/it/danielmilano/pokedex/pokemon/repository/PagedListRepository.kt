package it.danielmilano.pokedex.pokemon.repository

import androidx.paging.LivePagedListBuilder
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.datasource.PagedListDataSourceFactory
import it.danielmilano.pokedex.pokemon.model.PagedListResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import java.util.concurrent.Executors

class PagedListRepository(private val pokemonApi: PokemonApi, private val pokemonItemListDAO: PokemonItemListDAO) {

    fun getPagedList(dataType: String): PagedListResult<PokemonListItem> {
        val config = androidx.paging.PagedList.Config.Builder()
            .setInitialLoadSizeHint(4 * 2)
            .setEnablePlaceholders(false)
            .setPageSize(4)
            .setPrefetchDistance(4)
            .build()

        val pokemonListDataSourceFactory = PagedListDataSourceFactory(dataType, pokemonApi, pokemonItemListDAO)
        val result = LivePagedListBuilder(pokemonListDataSourceFactory, config)
            .setFetchExecutor(Executors.newFixedThreadPool(3))
            .build()

        return PagedListResult(
            result,
            pokemonListDataSourceFactory.isInitialLoading,
            pokemonListDataSourceFactory.isLoading,
            pokemonListDataSourceFactory.networkError
        )
    }
}