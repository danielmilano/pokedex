package it.danielmilano.pokedex.pokemon.repository

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.datasource.local.PokemonListItemBoundaryCallback
import it.danielmilano.pokedex.pokemon.model.PagedListResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem

class PagedListRepository(
    private val pokemonApi: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO
) {

    fun getPagedList(): PagedListResult<PokemonListItem> {
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(4 * 2)
            .setEnablePlaceholders(false)
            .setPageSize(4)
            .setPrefetchDistance(4)
            .build()

        val livePageListBuilder =
            LivePagedListBuilder<Int, PokemonListItem>(
                pokemonItemListDAO.all(),
                config
            )

        val boundaryCallback =
            PokemonListItemBoundaryCallback(
                pokemonApi,
                pokemonItemListDAO
            )

        return PagedListResult(
            livePageListBuilder.setBoundaryCallback(boundaryCallback).build(),
            boundaryCallback.isInitialLoading,
            boundaryCallback.isLoading,
            boundaryCallback.networkError
        )
    }
}