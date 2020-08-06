package it.danielmilano.pokedex.pokemon.model

enum class Status {
    IDLE,
    FIRST_LOADING,
    LOADING,
    SUCCESS,
    ERROR
}

data class NetworkState constructor(
    val status: Status,
    val msg: String? = null
) {
    companion object {
        val IDLE = NetworkState(Status.IDLE)
        val FIRST_LOADING = NetworkState(Status.FIRST_LOADING)
        val LOADING = NetworkState(Status.LOADING)
        val SUCCESS = NetworkState(Status.SUCCESS)
        val ERROR = NetworkState(Status.ERROR)
    }
}