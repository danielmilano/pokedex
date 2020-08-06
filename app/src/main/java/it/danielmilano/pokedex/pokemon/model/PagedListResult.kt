package it.danielmilano.pokedex.pokemon.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.base.BaseMutableLiveData

class PagedListResult<T>(
    val result: LiveData<PagedList<T>> = MutableLiveData<PagedList<T>>(),
    val networkState: LiveData<NetworkState> = BaseMutableLiveData(),
    val endReached: LiveData<Boolean> = BaseMutableLiveData(),
    val retry: () -> Unit
)