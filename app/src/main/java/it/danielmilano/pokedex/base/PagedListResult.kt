package it.danielmilano.pokedex.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList

class PagedListResult<T>(
    val result: LiveData<PagedList<T>> = MutableLiveData(),
    val networkState: LiveData<NetworkState> = MutableLiveData(),
    val error: LiveData<String?> = MutableLiveData(),
    val lastPage: LiveData<Boolean> = MutableLiveData(),
    val retry: () -> Unit
)