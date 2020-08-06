package it.danielmilano.pokedex.pokemon.datasource.remote

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.BaseMutableLiveData
import it.danielmilano.pokedex.database.dao.PokemonItemListDAO
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.model.PaginatedResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class PagedListDataSource(
    private val dataType: String,
    private val api: PokemonApi,
    private val pokemonItemListDAO: PokemonItemListDAO
) : PageKeyedDataSource<String, PokemonListItem>() {

    val isLoading = MutableLiveData<Boolean>()
    val isInitialLoading = MutableLiveData<Boolean>()
    val networkError = BaseMutableLiveData<String?>()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PokemonListItem>
    ) {
        isInitialLoading.postValue(true)
        api.getInitialDataList()
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
                                    pokemonItemListDAO.add(it.results)
                                }
                                callback.onResult(it.results, 0, it.count, it.previous, it.next)
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

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, PokemonListItem>
    ) {
        isLoading.postValue(true)
        api.getDataList(params.key).enqueue(object : Callback<PaginatedResult> {
            override fun onFailure(call: Call<PaginatedResult>, t: Throwable) {
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
                                pokemonItemListDAO.add(it.results)
                            }
                            callback.onResult(it.results, it.next)
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

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, PokemonListItem>
    ) {
    }

}