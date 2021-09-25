package it.danielmilano.pokedex.pokemon.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import it.danielmilano.pokedex.pokemon.adapter.PagedListAdapter
import it.danielmilano.pokedex.databinding.FragmentPokemonListBinding
import it.danielmilano.pokedex.base.NetworkState
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.base.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModel()

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.adapter = PagedListAdapter(this::navigateToPokemonDetail) { viewModel.retry() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun attachObservers() {
        with(viewModel) {
            networkState.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.IDLE,
                    Status.SUCCESS -> {
                        binding.progressBar.isVisible = false
                    }
                    Status.FIRST_LOADING -> {
                        binding.progressBar.isVisible = true
                    }
                    Status.LOADING,
                    Status.ERROR -> {
                        binding.progressBar.isVisible = false
                        binding.adapter?.setNetworkState(it)
                    }
                }
            })
            error.observe(viewLifecycleOwner, Observer {
                binding.progressBar.isVisible = false
                binding.adapter?.setNetworkState(
                    NetworkState(
                        Status.ERROR,
                        it
                    )
                )
            })
            pokemonList.observe(viewLifecycleOwner, Observer {
                binding.adapter?.submitList(it)
            })
            lastPage.observe(viewLifecycleOwner, Observer {
                binding.adapter?.setNetworkState(
                    NetworkState(
                        Status.SUCCESS
                    )
                )
            })
        }
    }

    private fun navigateToPokemonDetail(item: PokemonListItem) {
        val direction =
            PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(
                item.url,
                item.name
            )
        navController.navigate(direction)
    }
}