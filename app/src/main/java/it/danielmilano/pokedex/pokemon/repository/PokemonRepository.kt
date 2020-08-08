package it.danielmilano.pokedex.pokemon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.base.DataResponse
import it.danielmilano.pokedex.pokemon.model.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository(private val api: PokemonApi) {

    fun getPokemonDetail(url: String): LiveData<DataResponse<Pokemon>> {
        val result = MutableLiveData<DataResponse<Pokemon>>()
        api.getPokemonDetail(url).enqueue(object : Callback<Pokemon> {
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                result.value = DataResponse.Error(t.message.toString())
            }

            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                response.body()?.let {
                    result.value = DataResponse.Success(it)
                } ?: run {
                    result.value = DataResponse.Error(response.errorBody().toString())
                }
            }
        })
        return result
    }
}