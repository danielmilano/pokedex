package it.danielmilano.pokedex.pokemon.ui.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
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

    val pokemonDetail: MediatorLiveData<Pokemon> = MediatorLiveData()

    val isLoading: MediatorLiveData<Boolean> = MediatorLiveData()

    val errorMessage: MediatorLiveData<String> = MediatorLiveData()

    private lateinit var url: String

    fun assignArgument(args: PokemonDetailFragmentArgs) {
        url = args.url
        viewModelScope.launch {
            getPokemonByNameAsync(args.name).await()?.let {
                withContext(Dispatchers.Main) {
                    pokemonDetail.value = it
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
        errorMessage.value = null
        getPokemonDetail(url)
    }

    private fun getPokemonDetail(url: String) {
        getPokemonDetailUseCase(url).let { response ->
            isLoading.value = true
            isLoading.addSource(response) {
                isLoading.value = false
                when (response.value) {
                    is DataResponse.Success -> {
                        val result = (response.value as DataResponse.Success<Pokemon>).data
                        viewModelScope.launch {
                            withContext(Dispatchers.IO) {
                                pokemonDAO.add(result)
                            }
                            pokemonDetail.postValue(result)
                        }
                    }
                    is DataResponse.Error -> {
                        val result = (it as DataResponse.Error<Pokemon>).message
                        errorMessage.postValue(result)
                    }
                }
            }
        }
    }

    suspend fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return viewModelScope.async(Dispatchers.Default) { block() }
    }
}