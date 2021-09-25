package it.danielmilano.pokedex.pokemon.ui.detail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.danielmilano.pokedex.pokemon.model.Pokemon
import it.danielmilano.pokedex.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.*

class PokemonDetailViewModel(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    val pokemon: MediatorLiveData<Pokemon> = MediatorLiveData()

    val isLoading: MediatorLiveData<Boolean> = MediatorLiveData()

    val errorMessage: MediatorLiveData<String> = MediatorLiveData()

    private lateinit var name: String
    private lateinit var url: String

    fun retry() {
        getPokemonDetail(name, url)
    }

    fun getPokemonDetail(name: String, url: String) {
        this.name = name
        this.url = url
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = getPokemonDetailUseCase(name, url)
                isLoading.postValue(false)
                errorMessage.postValue(null)
                pokemon.postValue(result)
            } catch (e: Exception) {
                isLoading.postValue(false)
                pokemon.postValue(null)
                errorMessage.postValue(e.message)
            }
        }
    }
}