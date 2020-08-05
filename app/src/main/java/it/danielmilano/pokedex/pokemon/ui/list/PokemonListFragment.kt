package it.danielmilano.pokedex.pokemon.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import it.danielmilano.pokedex.R
import it.danielmilano.pokedex.pokemon.adapter.PagedListAdapter
import it.danielmilano.pokedex.databinding.FragmentPokemonListBinding
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonListFragment : Fragment() {

    val viewModel: PokemonListViewModel by viewModel()

    private lateinit var binding: FragmentPokemonListBinding

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = PagedListAdapter(this::navigateToPokemonDetail)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachObservers()
    }

    private fun attachObservers() {
        with(viewModel) {
            isInitialLoading.observe(viewLifecycleOwner, Observer {
                binding.progressBar.isVisible = it
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                binding.adapter?.loading(it)
            })

            networkError.observe(viewLifecycleOwner, Observer {
                Toast.makeText(
                    context,
                    it ?: getString(R.string.message_generic_error),
                    Toast.LENGTH_SHORT
                ).show()
            })

            pokemonList.observe(viewLifecycleOwner, Observer {
                binding.adapter?.submitList(it)
            })
        }
    }

    private fun navigateToPokemonDetail(item: PokemonListItem) {
        val direction =
            PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(item.url)
        navController.navigate(direction)
    }
}