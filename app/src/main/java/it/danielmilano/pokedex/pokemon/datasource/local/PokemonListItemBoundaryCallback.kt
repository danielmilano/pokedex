package it.danielmilano.pokedex.pokemon.datasource.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.base.NetworkState
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.base.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PokemonListItemBoundaryCallback constructor(
    private val api: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO,
    private var coroutineScope: CoroutineScope
) : PagedList.BoundaryCallback<PokemonListItem>() {

    val networkState = MutableLiveData<NetworkState>()
    val lastPage = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    private var retry: (() -> Any)? = null

    override fun onZeroItemsLoaded() {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                networkState.postValue(NetworkState(Status.FIRST_LOADING))
                val result = api.getInitialDataList()
                pokemonItemListDAO.add(result.resultsWithPage)
                networkState.postValue(NetworkState(Status.SUCCESS))
                retry = null
            } catch (e : Exception){
                onError(e.message, null)
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: PokemonListItem) {
        itemAtEnd.nextPage?.let {
            coroutineScope.launch(Dispatchers.IO) {
                try {
                    networkState.postValue(NetworkState(Status.LOADING))
                    val result = api.getDataList(it)
                    pokemonItemListDAO.add(result.resultsWithPage)
                    networkState.postValue(NetworkState(Status.SUCCESS))
                    retry = null
                } catch (e: Exception){
                    onError(e.message, itemAtEnd)
                }
            }
        } ?: run {
            networkState.postValue(NetworkState(Status.SUCCESS))
            lastPage.postValue(true)
        }
    }

    private fun onError(message: String?, itemAtEnd: PokemonListItem? = null) {
        error.postValue(message)
        itemAtEnd?.let {
            retry = { onItemAtEndLoaded(it) }
        } ?: run {
            retry = { onZeroItemsLoaded() }
        }
    }

    fun retryOnFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            coroutineScope.launch(Dispatchers.IO) {
                it.invoke()
            }
        }
    }
}