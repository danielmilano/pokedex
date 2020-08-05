package it.danielmilano.pokedex.base

import android.app.Application
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.danielmilano.pokedex.utils.NoObserverAttachedException

open class BaseViewModel<STATE, EFFECT, EVENT>(application: Application) :
    AndroidViewModel(application), BaseViewModelContract<EVENT> {

    private val _viewStates: MutableLiveData<STATE> = MutableLiveData()
    fun viewStates(): LiveData<STATE> = _viewStates

    private var _viewState: STATE? = null
    protected var viewState: STATE
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value
        }


    private val _viewEffects: BaseMutableLiveData<EFFECT> = BaseMutableLiveData()
    fun viewEffects(): BaseMutableLiveData<EFFECT> = _viewEffects

    private var _viewEffect: EFFECT? = null
    protected var viewEffect: EFFECT
        get() = _viewEffect
            ?: throw UninitializedPropertyAccessException("\"viewEffect\" was queried before being initialized")
        set(value) {
            _viewEffect = value
            _viewEffects.value = value
        }

    @CallSuper
    override fun process(viewEvent: EVENT) {
        if (!viewStates().hasObservers()) {
            throw NoObserverAttachedException("No observer attached. In case of custom View \"startObserving()\" function needs to be called manually.")
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}