package it.danielmilano.pokedex.pokemon.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.BaseMutableLiveData
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.model.PaginatedWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PagedListDataSource(
    private val dataType: String,
    private val api: PokemonApi
) : PageKeyedDataSource<String, PokemonListItem>() {

    val isLoading = MutableLiveData<Boolean>()
    val isInitialLoading = MutableLiveData<Boolean>()
    val networkError = BaseMutableLiveData<String?>()

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, PokemonListItem>
    ) {
        isInitialLoading.postValue(true)
        api.getInitialDataList(dataType)
            .enqueue(object : Callback<PaginatedWrapper<PokemonListItem>> {
                override fun onFailure(
                    call: Call<PaginatedWrapper<PokemonListItem>>,
                    t: Throwable
                ) {
                    isInitialLoading.postValue(false)
                    networkError.postValue(t.message)
                }

                override fun onResponse(
                    call: Call<PaginatedWrapper<PokemonListItem>>,
                    response: Response<PaginatedWrapper<PokemonListItem>>
                ) {
                    isInitialLoading.postValue(false)
                    response.body()?.let {
                        if (response.isSuccessful) {
                            if (response.isSuccessful) {
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
        api.getDataList(params.key).enqueue(object : Callback<PaginatedWrapper<PokemonListItem>> {
            override fun onFailure(call: Call<PaginatedWrapper<PokemonListItem>>, t: Throwable) {
                isLoading.postValue(false)
                networkError.postValue(t.message)
            }

            override fun onResponse(
                call: Call<PaginatedWrapper<PokemonListItem>>,
                response: Response<PaginatedWrapper<PokemonListItem>>
            ) {
                isLoading.postValue(false)
                response.body()?.let {
                    if (response.isSuccessful) {
                        if (response.isSuccessful) {
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