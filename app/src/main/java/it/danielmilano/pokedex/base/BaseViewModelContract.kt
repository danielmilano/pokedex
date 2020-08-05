package it.danielmilano.pokedex.base

internal interface BaseViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}
