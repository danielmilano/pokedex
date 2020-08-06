package it.danielmilano.pokedex.pokemon.datasource.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.BaseMutableLiveData
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.PaginatedResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class PokemonListItemBoundaryCallback constructor(
    private val api: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO
) : PagedList.BoundaryCallback<PokemonListItem>() {

    val isLoading = MutableLiveData<Boolean>()
    val isInitialLoading = MutableLiveData<Boolean>()
    val networkError = BaseMutableLiveData<String?>()
    val endReached = MutableLiveData<Boolean>()
    private var retry: (() -> Any)? = null

    /**
     * There is no sync on the state because paging will always call onZeroItemsLoaded first then wait
     * for it to return some success value before calling onItemAtEndLoaded.
     */
    override fun onZeroItemsLoaded() {
        isInitialLoading.postValue(true)
        api.getInitialDataList().enqueue(callback())
    }

    override fun onItemAtEndLoaded(itemAtEnd: PokemonListItem) {
        isLoading.postValue(true)
        itemAtEnd.nextPage?.let {
            api.getDataList(it).enqueue(callback(itemAtEnd))
        } ?: run {
            isLoading.postValue(false)
            endReached.postValue(true)
        }
    }

    private fun callback(itemAtEnd: PokemonListItem? = null) = object : Callback<PaginatedResult> {
        override fun onFailure(call: Call<PaginatedResult>, t: Throwable) {
            onError(false, t.message)
        }

        override fun onResponse(
            call: Call<PaginatedResult>,
            response: Response<PaginatedResult>
        ) {
            response.body()?.let { result ->
                if (response.isSuccessful) {
                    onSuccess(itemAtEnd?.let { false } ?: true, result.resultsWithPage)
                } else {
                    onError(itemAtEnd?.let { false } ?: true, response.errorBody().toString(), itemAtEnd)
                }
            } ?: run {
                onError(itemAtEnd?.let { false } ?: true, response.errorBody().toString())
            }
        }

    }

    private fun onError(isInitial: Boolean, message: String?, itemAtEnd: PokemonListItem? = null) {
        if (isInitial) isInitialLoading.postValue(false) else isLoading.postValue(false)
        networkError.postValue(message)
        itemAtEnd?.let { retry = { onItemAtEndLoaded(it) }
        } ?: run { retry = { onZeroItemsLoaded() } }
    }

    private fun onSuccess(isInitial: Boolean, pokemonListItem: List<PokemonListItem>) {
        if (isInitial) isInitialLoading.postValue(false) else isLoading.postValue(false)
        retry = null
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                pokemonItemListDAO.add(pokemonListItem)
            }
        }
    }

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    it.invoke()
                }
            }
        }
    }
}