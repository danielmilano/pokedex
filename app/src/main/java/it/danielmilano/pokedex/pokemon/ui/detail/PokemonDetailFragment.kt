package it.danielmilano.pokedex.pokemon.ui.detail

import androidx.fragment.app.viewModels
import it.danielmilano.pokedex.base.BaseFragment

class PokemonDetailFragment :
    BaseFragment<PokemonDetailViewState, PokemonDetailViewEffect, PokemonDetailViewEvent, PokemonDetailViewModel>() {

    override val viewModel: PokemonDetailViewModel by viewModels()

    override fun renderViewState(viewState: PokemonDetailViewState) {
        TODO("Not yet implemented")
    }

    override fun renderViewEffect(viewEffect: PokemonDetailViewEffect) {
        TODO("Not yet implemented")
    }

}