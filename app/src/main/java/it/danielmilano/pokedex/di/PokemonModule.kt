package it.danielmilano.pokedex.di

import androidx.lifecycle.viewModelScope
import it.danielmilano.pokedex.pokemon.repository.PagedListRepository
import it.danielmilano.pokedex.pokemon.repository.PokemonRepository
import it.danielmilano.pokedex.pokemon.ui.detail.PokemonDetailViewModel
import it.danielmilano.pokedex.pokemon.ui.list.PokemonListViewModel
import it.danielmilano.pokedex.usecase.GetPagedListUseCase
import it.danielmilano.pokedex.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pokemonModule = module {

    viewModel { PokemonListViewModel(get()) }
    viewModel { PokemonDetailViewModel(get()) }

    single { providePokemonListViewModelScope(get()) }

    factory { GetPagedListUseCase(get()) }
    single { PagedListRepository(get(), get(), get()) }
    single { PokemonRepository(get()) }
    factory { GetPokemonDetailUseCase(get(), get()) }
}

private fun providePokemonListViewModelScope(pokemonDetailViewModel: PokemonDetailViewModel): CoroutineScope {
    return pokemonDetailViewModel.viewModelScope
}

