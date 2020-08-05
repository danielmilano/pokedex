package it.danielmilano.pokedex.pokemon.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import it.danielmilano.pokedex.base.BaseFragment
import it.danielmilano.pokedex.databinding.FragmentPokemonDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonDetailFragment :
    BaseFragment<PokemonDetailViewState, PokemonDetailViewEffect, PokemonDetailViewEvent, PokemonDetailViewModel>() {

    override val viewModel: PokemonDetailViewModel by viewModel()

    private lateinit var binding: FragmentPokemonDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun renderViewState(viewState: PokemonDetailViewState) {
        when (viewState.fetchStatus) {
            is FetchStatus.Fetched -> {
                binding.pokemon = viewState.pokemon
            }
            is FetchStatus.NotFetched -> {
                arguments?.let {
                    viewModel.process(
                        PokemonDetailViewEvent.FetchPokemonDetail(
                            PokemonDetailFragmentArgs.fromBundle(it).url
                        )
                    )
                }
            }
            is FetchStatus.Fetching -> {

            }
        }
    }

    override fun renderViewEffect(viewEffect: PokemonDetailViewEffect) {
        when (viewEffect) {
            is PokemonDetailViewEffect.ShowSnackbar -> {
                Snackbar.make(binding.root, viewEffect.message, Snackbar.LENGTH_SHORT).show()
            }
            is PokemonDetailViewEffect.ShowToast -> {
                Toast.makeText(context, viewEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}