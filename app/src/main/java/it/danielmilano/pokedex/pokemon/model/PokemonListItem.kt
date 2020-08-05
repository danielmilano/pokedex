package it.danielmilano.pokedex.pokemon.model

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import it.danielmilano.pokedex.utils.ListStringConverter

@Entity
@TypeConverters(ListStringConverter::class)
data class PokemonListItem(
    @PrimaryKey @NonNull val name: String,
    val url: String,
    var nextPage : String?
) {
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PokemonListItem> = object : DiffUtil.ItemCallback<PokemonListItem>() {
            override fun areItemsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = (oldItem.url == newItem.url)

            override fun areContentsTheSame(oldItem: PokemonListItem, newItem: PokemonListItem): Boolean = oldItem == newItem
        }
    }
}