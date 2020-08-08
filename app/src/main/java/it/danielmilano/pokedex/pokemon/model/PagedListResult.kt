package it.danielmilano.pokedex.pokemon.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList

class PagedListResult<T>(
    val result: LiveData<PagedList<T>> = MutableLiveData<PagedList<T>>(),
    val networkState: LiveData<NetworkState> = MutableLiveData(),
    val error: LiveData<String> = MutableLiveData(),
    val endReached: LiveData<Boolean> = MutableLiveData(),
    val retry: () -> Unit
)