package it.danielmilano.pokedex.base

sealed class DataResponse<out T> {
    data class Success<T>(val data: T) : DataResponse<T>()
    data class Error<T>(val message: String) : DataResponse<T>()
}