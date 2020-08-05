package it.danielmilano.pokedex.pokemon.datasource.local

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.BaseMutableLiveData
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.PaginatedResult
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class PokemonListItemBoundaryCallback constructor(
    private val api: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO
) : PagedList.BoundaryCallback<PokemonListItem>() {

    val isLoading = MutableLiveData<Boolean>()
    val isInitialLoading = MutableLiveData<Boolean>()
    val networkError = BaseMutableLiveData<String?>()

    override fun onZeroItemsLoaded() {
        isInitialLoading.postValue(true)
        api.getInitialDataList("pokemon")
            .enqueue(object : Callback<PaginatedResult> {
                override fun onFailure(
                    call: Call<PaginatedResult>,
                    t: Throwable
                ) {
                    isInitialLoading.postValue(false)
                    networkError.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<PaginatedResult>,
                    response: Response<PaginatedResult>
                ) {
                    isInitialLoading.postValue(false)
                    response.body()?.let {
                        if (response.isSuccessful) {
                            if (response.isSuccessful) {
                                thread {
                                    pokemonItemListDAO.add(it.resultsWithPage)
                                }
                            } else {
                                networkError.postValue(response.errorBody().toString())
                            }
                        } else {
                            networkError.postValue(response.message())
                        }
                    } ?: run {
                        networkError.postValue("Network error!")
                    }
                }
            })
    }

    override fun onItemAtEndLoaded(itemAtEnd: PokemonListItem) {
        isLoading.postValue(true)
        itemAtEnd.nextPage?.let {
            api.getDataList(it).enqueue(object : Callback<PaginatedResult> {
                override fun onFailure(
                    call: Call<PaginatedResult>,
                    t: Throwable
                ) {
                    isLoading.postValue(false)
                    networkError.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<PaginatedResult>,
                    response: Response<PaginatedResult>
                ) {
                    isLoading.postValue(false)
                    response.body()?.let {
                        if (response.isSuccessful) {
                            if (response.isSuccessful) {
                                thread {
                                    pokemonItemListDAO.add(it.resultsWithPage)
                                }
                            } else {
                                networkError.postValue(response.errorBody().toString())
                            }
                        } else {
                            networkError.postValue(response.message())
                        }
                    } ?: run {
                        networkError.postValue("Network error!")
                    }
                }

            })
        }
    }
}