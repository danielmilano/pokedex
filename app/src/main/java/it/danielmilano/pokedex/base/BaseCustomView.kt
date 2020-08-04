package it.danielmilano.pokedex.base

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

abstract class BaseCustomView<STATE, EFFECT, EVENT, ViewModel : BaseViewModel<STATE, EFFECT, EVENT>> {

    abstract val viewModel: ViewModel

    private val viewStateObserver = Observer<STATE> {
        renderViewState(it)
    }

    private val viewEffectObserver = Observer<EFFECT> {
        renderViewEffect(it)
    }

    fun startObserving(lifecycleOwner: LifecycleOwner) {
        viewModel.viewStates().observe(lifecycleOwner, viewStateObserver)
        viewModel.viewEffects().observe(lifecycleOwner, viewEffectObserver)
    }

    abstract fun renderViewState(viewState: STATE)

    abstract fun renderViewEffect(viewEffect: EFFECT)
}