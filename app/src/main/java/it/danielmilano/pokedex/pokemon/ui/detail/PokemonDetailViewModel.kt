package it.danielmilano.pokedex.pokemon.ui.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.danielmilano.pokedex.base.DataResponse
import it.danielmilano.pokedex.database.dao.PokemonDAO
import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.*

class PokemonDetailViewModel(
    private val pokemonDAO: PokemonDAO,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    val pokemon: MediatorLiveData<Pokemon> = MediatorLiveData()

    val isLoading: MediatorLiveData<Boolean> = MediatorLiveData()

    val errorMessage: MediatorLiveData<String> = MediatorLiveData()

    private lateinit var url: String

    fun getPokemonDetail(args: PokemonDetailFragmentArgs) {
        url = args.url
        viewModelScope.launch {
            getPokemonByNameAsync(args.name).await()?.let {
                withContext(Dispatchers.Main) {
                    pokemon.value = it
                }
            } ?: run {
                getPokemonDetail(args.url)
            }
        }
    }

    private suspend fun getPokemonByNameAsync(name: String): Deferred<Pokemon?> {
        return async {
            pokemonDAO.getByName(name)
        }
    }

    fun retry() {
        getPokemonDetail(url)
    }

    private fun getPokemonDetail(url: String) {
        isLoading.value = true
        getPokemonDetailUseCase(url).let { response ->
            isLoading.addSource(response) {
                isLoading.value = false
            }
            pokemon.addSource(response) {
                if (response.value is DataResponse.Success<Pokemon>) {
                    val result = (response.value as DataResponse.Success<Pokemon>).data
                    errorMessage.value = null
                    pokemon.value = result
                    viewModelScope.launch {
                        withContext(Dispatchers.IO) {
                            pokemonDAO.add(result)
                        }
                    }
                }
            }
            errorMessage.addSource(response) {
                if (response.value is DataResponse.Error<Pokemon>) {
                    pokemon.value = null
                    errorMessage.value = (response.value as DataResponse.Error<Pokemon>).message
                }
            }
        }
    }

    private suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return viewModelScope.async(Dispatchers.Default) { block() }
    }
}