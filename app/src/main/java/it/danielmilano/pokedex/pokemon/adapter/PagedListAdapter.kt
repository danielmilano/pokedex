package it.danielmilano.pokedex.pokemon.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import it.danielmilano.pokedex.R
import it.danielmilano.pokedex.databinding.ItemLoaderBinding
import it.danielmilano.pokedex.databinding.ItemPokemonBinding
import it.danielmilano.pokedex.pokemon.model.NetworkState
import it.danielmilano.pokedex.pokemon.model.PokemonListItem
import it.danielmilano.pokedex.pokemon.model.Status

class PagedListAdapter(
    private val onItemClick: (PokemonListItem) -> Unit,
    private val retryCallback: () -> Unit
) :
    PagedListAdapter<PokemonListItem, it.danielmilano.pokedex.pokemon.adapter.PagedListAdapter.ItemsViewHolder>(
        PokemonListItem.DIFF_CALLBACK
    ) {

    private var networkState: NetworkState = NetworkState.IDLE

    private fun hasExtraRow() = networkState == NetworkState.LOADING

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_loader
        } else {
            R.layout.item_pokemon
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_pokemon ->
                PokemonItemViewHolder(
                    ItemPokemonBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                ) {
                    getItem(it)?.let { item -> onItemClick(item) }
                }
            R.layout.item_loader -> LoaderItemHolder(
                ItemLoaderBinding.inflate(layoutInflater, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_pokemon -> {
                val item = getItem(position)
                item?.let {
                    (holder as PokemonItemViewHolder).bind(item)
                }
            }
            R.layout.item_loader -> {
                (holder as LoaderItemHolder).bind(networkState, retryCallback)
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    open inner class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class PokemonItemViewHolder(
        private val binding: ItemPokemonBinding,
        private val onItemClick: (Int) -> Unit
    ) : ItemsViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
        }

        fun bind(pokemon: PokemonListItem) {
            binding.pokemon = pokemon
        }
    }

    inner class LoaderItemHolder(
        private val binding: ItemLoaderBinding
    ) : ItemsViewHolder(binding.root) {

        fun bind(networkState: NetworkState?, retryCallback: () -> Unit) {
            binding.progressBar.visibility = toVisibility(networkState?.status == Status.LOADING)
            binding.retry.visibility = toVisibility(networkState?.status == Status.ERROR)
            binding.error.visibility = toVisibility(networkState?.msg != null)
            binding.error.text = binding.error.context.getString(R.string.network_not_available)
            binding.retry.setOnClickListener { retryCallback() }
        }

        private fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}