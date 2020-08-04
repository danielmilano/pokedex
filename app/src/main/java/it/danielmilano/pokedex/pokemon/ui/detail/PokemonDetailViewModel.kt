package it.danielmilano.pokedex.pokemon.ui.detail

import android.app.Application
import it.danielmilano.pokedex.base.BaseViewModel
import it.danielmilano.pokedex.pokemon.repository.PagedListRepository

class PokemonDetailViewModel(application: Application, private val repository: PagedListRepository) :
    BaseViewModel<PokemonDetailViewState, PokemonDetailViewEffect, PokemonDetailViewEvent>(application) {

}