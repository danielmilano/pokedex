package it.danielmilano.pokedex.pokemon.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import it.danielmilano.pokedex.base.BaseMutableLiveData

class PagedListResult<T>(
        val result: LiveData<PagedList<T>> = MutableLiveData<PagedList<T>>(),
        val isInitialLoading: LiveData<Boolean> = MutableLiveData(),
        val isLoading: LiveData<Boolean> = MutableLiveData(),
        val networkError: LiveData<String?> = BaseMutableLiveData(),
        val endReached: LiveData<Boolean> = MutableLiveData(),
        val retry: () -> Unit
)