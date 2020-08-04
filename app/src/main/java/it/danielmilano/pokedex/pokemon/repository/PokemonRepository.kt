package it.danielmilano.pokedex.pokemon.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.danielmilano.pokedex.api.PokemonApi
import it.danielmilano.pokedex.pokemon.model.Pokemon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository(private val api: PokemonApi) {

    fun getPokemonDetail(url: String): LiveData<Pokemon> {
        val result = MutableLiveData<Pokemon>()
        api.getPokemonDetail(url).enqueue(object : Callback<Pokemon> {
            override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                result.value = null
            }

            override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                result.value = response.body()
            }
        })
        return result
    }
}