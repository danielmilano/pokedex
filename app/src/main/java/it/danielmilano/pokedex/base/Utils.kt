package it.danielmilano.pokedex.base

internal interface ViewModelContract<EVENT> {
    fun process(viewEvent: EVENT)
}

class NoObserverAttachedException(message: String) : Exception(message)