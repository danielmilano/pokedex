package it.danielmilano.pokedex.pokemon.datasource.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.NetworkState
import it.danielmilano.pokedex.pokemon.model.PaginatedResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListItemBoundaryCallback constructor(
    private val api: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO,
    private var coroutineScope: CoroutineScope
) : PagedList.BoundaryCallback<PokemonListItem>() {

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()
    val endReached = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    private var retry: (() -> Any)? = null

    override fun onZeroItemsLoaded() {
        networkState.postValue(NetworkState(Status.FIRST_LOADING))
        api.getInitialDataList().enqueue(callback())
    }

    override fun onItemAtEndLoaded(itemAtEnd: PokemonListItem) {
        networkState.postValue(NetworkState(Status.LOADING))
        itemAtEnd.nextPage?.let {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    api.getDataList(it).enqueue(callback(itemAtEnd))
                }
            }
        } ?: run {
            networkState.postValue(NetworkState(Status.SUCCESS))
            endReached.postValue(true)
        }
    }


    private fun callback(itemAtEnd: PokemonListItem? = null) = object : Callback<PaginatedResult> {
        override fun onFailure(call: Call<PaginatedResult>, t: Throwable) {
            onError(t.message, itemAtEnd)
        }

        override fun onResponse(
            call: Call<PaginatedResult>,
            response: Response<PaginatedResult>
        ) {
            response.body()?.let { result ->
                if (response.isSuccessful) {
                    onSuccess(result.resultsWithPage)
                } else {
                    onError(response.errorBody().toString(), itemAtEnd)
                }
            } ?: run {
                onError(response.errorBody().toString(), itemAtEnd)
            }
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

    private fun onSuccess(pokemonListItem: List<PokemonListItem>) {
        networkState.postValue(NetworkState(Status.SUCCESS))
        retry = null
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                pokemonItemListDAO.add(pokemonListItem)
            }
        }
    }

    fun retryOnFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            coroutineScope.launch {
                withContext(Dispatchers.IO) {
                    it.invoke()
                }
            }
        }
    }
}