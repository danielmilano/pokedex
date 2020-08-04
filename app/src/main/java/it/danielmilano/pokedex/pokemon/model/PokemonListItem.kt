package it.danielmilano.pokedex.pokemon.model

import androidx.recyclerview.widget.DiffUtil

data class PokemonListItem(
        val name: String,
        val url: String
) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PokemonListItem> = object : DiffUtil.ItemCallback<PokemonListItem>() {
            override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = (oldItem.url == newItem.url)

            override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = oldItem == newItem
        }
    }
}