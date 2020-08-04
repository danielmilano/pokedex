package it.danielmilano.pokedex.usecase

import it.danielmilano.pokedex.pokemon.repository.PagedListRepository

class GetPagedListUseCase(private val repository: PagedListRepository) {
    operator fun invoke(dataType: String) = repository.getPagedList(dataType)
}
