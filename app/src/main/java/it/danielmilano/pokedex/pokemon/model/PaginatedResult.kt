package it.danielmilano.pokedex.pokemon.model

data class PaginatedResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
) {
    val resultsWithPage: List<PokemonListItem>
        get() = results.apply { forEach { it.nextPage = next } }

}