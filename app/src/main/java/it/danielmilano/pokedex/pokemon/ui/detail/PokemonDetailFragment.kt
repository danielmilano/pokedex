package it.danielmilano.pokedex.pokemon.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import it.danielmilano.pokedex.databinding.FragmentPokemonDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonDetailFragment : Fragment() {

    private lateinit var binding: FragmentPokemonDetailBinding

    private val viewModel: PokemonDetailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel.apply {
            arguments?.let { assignArgument(PokemonDetailFragmentArgs.fromBundle(it)) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retry.setOnClickListener {
            viewModel.retry()
        }
    }
}